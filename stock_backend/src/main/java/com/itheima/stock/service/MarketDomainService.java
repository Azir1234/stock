package com.itheima.stock.service;

import com.itheima.stock.pojo.domin.*;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface MarketDomainService {
    R<List<InnerMarketDomain>> getInnerMarketInfo();

    R<List<StockBlockDomain>> getStockBlockInfo();

    R<PageResult> getStockAllPage(Integer page, Integer pageSize);

    R<List<StockUpdownDomain>> getStockIncrease();

    R<Map<String,List>> getUpDownCount();

    void stockExport(HttpServletResponse response, Integer page, Integer pageSize);

    R<Map> getStockTradeAmt();

    R<Map> getStockUpdown();

    R<List<Stock4MinuteDomain>> getStockTimeSharing(String code);

    R<List<Stock4EvrDayDomain>> getStockScreenDkline(String code);

    R<List<SOMIInfoDomain>> getStockOutMarketIndex();

    R<List<Map>> getRelatedStockInformation(String searchStr);

    R<StockBusinessDomain> getMainBusinessOfIndividualStocks(String code);

    R<List<StockKlineDomain>> getStockKlineDomain(String code);
}
