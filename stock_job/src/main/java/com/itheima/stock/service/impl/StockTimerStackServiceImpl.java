package com.itheima.stock.service.impl;


import com.google.common.collect.Lists;
import com.itheima.stock.config.TaskExecutePool;
import com.itheima.stock.face.StockCacheFace;
import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.pojo.entity.StockBlockRtInfo;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import com.itheima.stock.pojo.entity.StockRtInfo;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.pojo.vo.TaskThreadPoolInfo;
import com.itheima.stock.service.StockTimerStackService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.utils.ParseType;
import com.itheima.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockTimerStackServiceImpl implements StockTimerStackService {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private StockCacheFace stockCacheFace;

    private HttpEntity<Object> httpEntity;

    @Override
    public void getInnerMarketInfo(){

        //先塞入request的头
        String url= stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getInner());

        ResponseEntity<String> responseEntity =restTemplate.exchange(url, HttpMethod.GET,httpEntity,String.class);

        if( responseEntity.getStatusCodeValue()!=200){
            System.out.println("获取失败");
        }
        //数据
        String body = responseEntity.getBody();

        String reg="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        //匹配字符串
        Matcher matcher = pattern.matcher(body);
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        //判断是否有匹配的数值
        while (matcher.find()){
            //获取大盘的code
            String marketCode = matcher.group(1);
            //获取其它信息，字符串以逗号间隔
            String otherInfo=matcher.group(2);
            //以逗号切割字符串，形成数组
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName=splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint=new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint=new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint=new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint=new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint=new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt=Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol=new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //组装entity对象
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            //收集封装的对象，方便批量插入
            list.add(info);
        }
        log.info("采集的当前大盘数据：{}",list);
        //批量插入
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        int count= 0;
        try {
            count = stockMarketIndexInfoMapper.insertBatch(list);
        } catch (Exception e) {
            System.out.println("数据已存在");
        }
        rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());

        if(count>0) System.out.println("成功");
        return;

    }
    @Override
    public void getStockRtInfo() {
        //发送请求
            //先获取url
//        List<String> list=stockBusinessMapper.selectCodeList();
//        List<String> collect = list.stream().map(code -> "sh" + code).collect(Collectors.toList());
        List<String> collect=stockCacheFace.getAllStockCodeWithPredix();
        Long startTime=System.currentTimeMillis();

        Lists.partition(collect, 15).forEach(codes->{
            threadPoolTaskExecutor.execute(()->{
                String url=stockInfoConfig.getMarketUrl() +String.join(",",codes);
                ResponseEntity<String> responseEntity=restTemplate.exchange(url,HttpMethod.GET,this.httpEntity,String.class);
                String respBody=responseEntity.getBody();
                List<StockRtInfo> stockRtInfos = parserStockInfoUtil.parser4StockOrMarketInfo(respBody, ParseType.ASHARE);
                try {
                    int insert = stockRtInfoMapper.insertBatch(stockRtInfos);
                } catch (Exception e) {
                    System.out.println("数据已存在");
                }
            });

        });


            //再发请求


        Long takeTime=System.currentTimeMillis()-startTime;
        log.info("花费时间:{}ms",takeTime);
    }
    @Override
    public void getBlockRtInfo(){
        //先获取url
        String blockUrl = stockInfoConfig.getBlockUrl();
       String body= restTemplate.getForObject(blockUrl,String.class);
        //String body = blockRt.getBody();
        List<StockBlockRtInfo> stockBlockRtInfos = parserStockInfoUtil.parse4StockBlock(body);
        try {
            int count= stockBlockRtInfoMapper.insertBatch(stockBlockRtInfos);

        } catch (Exception e) {
            log.info("插入失败");
        }


    }
    @PostConstruct
    public void InitHttpEntity(){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        this.httpEntity=new HttpEntity<>(headers);
        return;
    }
}
