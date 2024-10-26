package com.itheima.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.*;
import com.itheima.stock.pojo.domin.*;
import com.itheima.stock.pojo.entity.StockBusiness;
import com.itheima.stock.pojo.entity.StockRtInfo;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.MarketDomainService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Service
public class MarketDomainServiceImpl implements MarketDomainService {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
       R<List<InnerMarketDomain>> data= (R<List<InnerMarketDomain>>) caffeineCache.get("innerMarketInfos", key->{
            //通过这个去搜
            //两个数据，大盘和时间
            //时间
            DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
            Date date = lastDate4Stock.toDate();
            System.out.println(date);
            date=DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

            List<String> inner = stockInfoConfig.getInner();
            List<InnerMarketDomain> list= stockMarketIndexInfoMapper.getInnerMarketInfo(date,inner);
            return R.ok(list);
        });
        return data;

    }

    @Override
    public R<List<StockBlockDomain>> getStockBlockInfo() {
        List<StockBlockDomain> slist= stockBlockRtInfoMapper.selectByTimeAndMoney();
       //selct * from stock_table order by time,money limit 10;
        return R.ok(slist);
    }

    @Override
    public R<PageResult> getStockAllPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curDate = lastDate4Stock.toDate();
        curDate=DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        List<StockUpdownDomain> updownDomains=stockMarketIndexInfoMapper.getStockAllPage(curDate);
        PageInfo<StockUpdownDomain> pageInfo=new PageInfo<>(updownDomains);
        PageResult<StockUpdownDomain> pageResult=new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    @Override
    public R<List<StockUpdownDomain>> getStockIncrease() {
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curDate = lastDate4Stock.toDate();
        curDate=DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();


        List<StockUpdownDomain> list= stockMarketIndexInfoMapper.getStockIncrease(curDate);
        return R.ok(list);
    }

    @Override
    public R<Map<String,List>> getUpDownCount(){
        //flag
        //最新时间
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curTime = curDateTime.toDate();
        //TODO
        curTime= DateTime.parse("2021-12-30 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.2 获取最新交易时间对应的开盘时间
        DateTime openDate = DateTimeUtil.getOpenDate(curDateTime);
        Date openTime = openDate.toDate();
        //TODO
        openTime= DateTime.parse("2021-12-30 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> upCount=stockRtInfoMapper.getStockUpDownCount( 1, openTime, curTime);

        List<Map> downCount=stockRtInfoMapper.getStockUpDownCount(0,openTime,curTime);
        Map<String,List> map=new HashMap<>();
        map.put("upList",upCount);
        map.put("downList",downCount);
        return R.ok(map);
    }

    @Override
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
     /*   DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curTime = curDateTime.toDate();
        //TODO
        curTime= DateTime.parse("2021-12-30 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        */
        //先拿分页数据
        //1.List<StockUpdownDomain> infos=  stockMarketIndexInfoMapper.getStockAllPage(curTime);
        //2.调用之前用过的方法
        R<PageResult> stockAllPage = this.getStockAllPage(page, pageSize);
        List infos = stockAllPage.getData().getRows();

        //返回响应错误数据
        if (CollectionUtils.isEmpty(infos)) {
            //响应提示信息
            response.setContentType("application/json");

            response.setCharacterEncoding("utf-8");
            R<Object> r = R.error(ResponseCode.NO_RESPONSE_DATA);

            try {
                response.getWriter().write(new ObjectMapper().writeValueAsString(r));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //infos数据
        try {
            EasyExcel.write(response.getOutputStream(),StockUpdownDomain.class)
                    .sheet("用户信息").doWrite(infos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public R<Map> getStockTradeAmt() {
        //T日当前时间
        //DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //Date endTime=lastDate4Stock.toDate();
        DateTime lastDate4Stock=DateTime.parse("2021-12-28 14:25:00",
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime=lastDate4Stock.toDate();
        //T日开盘时间
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDate4Stock);
        Date openTime = openDateTime.toDate();
        //T-1
        DateTime preDateTime = DateTimeUtil.getPreDateTime(lastDate4Stock);

        Date endTime1 =  DateTimeUtil.getCloseDate(preDateTime).toDate();
        DateTime openDateTime1=DateTimeUtil.getOpenDate(preDateTime);
        Date openTime1=openDateTime1.toDate();

        List<Map> tlist =stockMarketIndexInfoMapper.getStockTradeAmt(openTime,endTime);
        List<Map> t1list=stockMarketIndexInfoMapper.getStockTradeAmt(openTime1,endTime1);
        Map<String,List<Map>> map=new HashMap<>();
        map.put("amtList",tlist);
        map.put("yesAmtList",t1list);

        return R.ok(map);
    }

    @Override
    public R<Map> getStockUpdown() {
        //时间
        //DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //Date curTime=lastDate4Stock.toDate();
        DateTime dateTime= DateTime.parse("2021-12-30 09:51:00",
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curTime=dateTime.toDate();
        List<Map> maps= stockRtInfoMapper.getStockUpdown(curTime);


        List<String> orderSections = stockInfoConfig.getUpDownRange();
        //思路：利用List集合的属性，然后顺序编译，找出每个标题对应的map，然后维护到一个新的List集合下即可
        List<Map> orderMaps =new ArrayList<>();
        for (String title : orderSections) {
            Map map=null;
            for (Map m : maps) {
                if (m.containsValue(title)) {
                    map=m;
                    break;
                }
            }
            if (map==null) {
                map=new HashMap();
                map.put("count",0);
                map.put("title",title);
            }
            orderMaps.add(map);
        }


        Map<String,Object> map=new HashMap<>();
        map.put("time",curTime);
        map.put("infos",orderMaps);




        return R.ok(map);
    }

    @Override
    public R<List<Stock4MinuteDomain>> getStockTimeSharing(String code) {
        //时间
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());

        lastDate4Stock=DateTime.parse("2021-12-30 14:25:00",
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime=lastDate4Stock.toDate();

        DateTime openDate = DateTimeUtil.getOpenDate(lastDate4Stock);
        Date openTime=openDate.toDate();

       List<Stock4MinuteDomain> sList=  stockRtInfoMapper.getStockTimeSharing(code,openTime,endTime);
        return R.ok(sList);
    }

    @Override
    public R<List<Stock4EvrDayDomain>> getStockScreenDkline(String code) {
        //时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endTime = endDateTime.toDate();
        //TODO MOCKDATA
        endTime=DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.2 获取开始时间
        DateTime startDateTime = endDateTime.minusDays(10);
        Date startTime = startDateTime.toDate();
        //TODO MOCKDATA
        startTime=DateTime.parse("2021-01-01 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        List<Stock4EvrDayDomain> slist=stockRtInfoMapper.getStockScreenDkline(code,startTime,endTime);


        return R.ok(slist);
    }

    @Override
    public R<List<SOMIInfoDomain>> getStockOutMarketIndex() {
        //功能描述：外盘指数行情数据查询，根据时间和大盘点数降序排序取前4

        //select * from market order by cur_time desc, cur_point desc limit 4
        List<SOMIInfoDomain> slist=stockOuterMarketIndexInfoMapper.getStockOutMarketIndex();
        return R.ok(slist);
    }

    @Override
    public R<List<Map>> getRelatedStockInformation(String searchStr) {

        //输入框输入股票编码后，显示关联的股票信息;
        //select * from
        List<Map> map= stockBusinessMapper.selectCodeNameList(searchStr);

        return R.ok(map);
    }

    @Override
    public R<StockBusinessDomain> getMainBusinessOfIndividualStocks(String code) {

        StockBusinessDomain stockBusinessDomain= stockBusinessMapper.selectByCode(code);

        return R.ok(stockBusinessDomain);
    }

    @Override
    public R<List<StockKlineDomain>> getStockKlineDomain(String code) {
        //获取时间
            //先判断今天是不是工作日
            //今天是
        System.out.println(DateTime.now());


        //结果是一个开始时间一个结束时间




        return R.ok();
    }
}
