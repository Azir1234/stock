package com.itheima.stock.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.Page;
import com.itheima.stock.pojo.domin.*;
import com.itheima.stock.service.MarketDomainService;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quot")
public class MarketDomainController {


    @Autowired
    private MarketDomainService marketDomainService;

    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
       return marketDomainService.getInnerMarketInfo();
    }
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> getStockBlockInfo(){
        return marketDomainService.getStockBlockInfo();
    }

    @GetMapping("/stock/all")
    public R<PageResult> getStockAllPage(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                               @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize){
        return marketDomainService.getStockAllPage(page,pageSize);
    }

    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockIncrease(){
        return marketDomainService.getStockIncrease();
    }
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getUpDownCount(){
        return marketDomainService.getUpDownCount();
    }
    @GetMapping("/stock/export")
    public void stockExport(HttpServletResponse response,@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "20")  Integer pageSize){
         marketDomainService.stockExport(response,page,pageSize);
    }

    @GetMapping("/stock/tradeAmt")
    public R<Map> stockTradeAmt(){
      return  marketDomainService.getStockTradeAmt();
    }
    @GetMapping("/stock/updown")
    public R<Map> getStockUpdown(){
        return marketDomainService.getStockUpdown();
    }

    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> getStockTimeSharing(@RequestParam(value = "code",required = true) String code){
        return marketDomainService.getStockTimeSharing(code);
    }

    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockScreenDkline(@RequestParam(value = "code",required = true) String code){
        return marketDomainService.getStockScreenDkline(code);
    }

    @GetMapping("/external/index")
    public R<List<SOMIInfoDomain>> getStockOutMarketIndex(){
        return marketDomainService.getStockOutMarketIndex();
    }

    @GetMapping("/stock/search")
    public R<List<Map>> getRelatedStockInformation(String searchStr){
        return marketDomainService.getRelatedStockInformation(searchStr);
    }

    @GetMapping("/stock/describe")
    public R<StockBusinessDomain> getMainBusinessOfIndividualStocks(String code){
        return marketDomainService.getMainBusinessOfIndividualStocks(code);
    }
    public R<List<StockKlineDomain>> getStockKlineDomain(String code)
    {
        return marketDomainService.getStockKlineDomain(code);
    }}
