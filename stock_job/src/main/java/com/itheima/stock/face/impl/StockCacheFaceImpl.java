package com.itheima.stock.face.impl;

import com.itheima.stock.face.StockCacheFace;
import com.itheima.stock.mapper.StockBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component("stockCacheFace")
public class StockCacheFaceImpl implements StockCacheFace {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Cacheable(cacheNames = "stock" ,key = "'stockCodes'")
    @Override
    public List<String> getAllStockCodeWithPredix() {
        List<String> list=stockBusinessMapper.selectCodeList();
        List<String> collect = list.stream().map(code -> "sh" + code).collect(Collectors.toList());
        return collect;
    }
}
