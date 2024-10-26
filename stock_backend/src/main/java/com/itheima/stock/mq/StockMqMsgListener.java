package com.itheima.stock.mq;


import com.github.benmanes.caffeine.cache.Cache;
import com.itheima.stock.service.MarketDomainService;
import com.itheima.stock.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class StockMqMsgListener {
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Autowired
    private MarketDomainService marketDomainService;

    @RabbitListener(queues = "innerMarketQueue")
    public void refreshInnerMarket(Date date){
        long l = DateTime.now().getMillis() - new DateTime(date).getMillis();
        if(l>600000l){
            log.info("数据太老了");
        }
        caffeineCache.invalidate("innerMarketInfos");
        marketDomainService.getInnerMarketInfo();

    }

}
