package com.itheima.stock.job;

import com.itheima.stock.service.StockTimerStackService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockJob {
    @Autowired
    private StockTimerStackService stockTimerStackService;

    @XxlJob("myJobHandler")
    public void demoJobHandler() throws Exception {

        System.out.println("当前时间"+ DateTime.now().toString("YYYY-MM-dd HH:mm:ss"));
        // default success
    }
    @XxlJob("InnerInfoJobHandler")
    public void InnerInfoJobHandler() throws Exception {
        stockTimerStackService.getInnerMarketInfo();
    }

    @XxlJob("BlockRtInfoJobHandler")
    public void BlockRtInfoJobHandler() throws Exception {
        stockTimerStackService.getBlockRtInfo();
    }
    @XxlJob("StockRtInfoJobHandler")
    public void StockRtInfoJobHandler() throws Exception {
        stockTimerStackService.getStockRtInfo();
    }
}
