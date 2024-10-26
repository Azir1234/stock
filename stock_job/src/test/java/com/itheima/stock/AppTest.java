package com.itheima.stock;

import com.google.common.collect.Lists;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.service.StockTimerStackService;
import com.itheima.stock.service.impl.StockTimerStackServiceImpl;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
  @Autowired
  private StockTimerStackService stockTimerStackService;
  @Test
  public void test() throws InterruptedException {
   /* List<String> list=stockBusinessMapper.selectCodeList();
      List<String> collect = list.stream().map(code -> "sh" + code).collect(Collectors.toList());

      List<List<String>> partition = Lists.partition(collect, 15);
      System.out.println(partition);*/
      stockTimerStackService.getStockRtInfo();
      Thread.sleep(1000);

  }
}
