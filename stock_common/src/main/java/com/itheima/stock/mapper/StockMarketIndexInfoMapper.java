package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domin.InnerMarketDomain;
import com.itheima.stock.pojo.domin.StockBlockDomain;
import com.itheima.stock.pojo.domin.StockUpdownDomain;
import com.itheima.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author AZIR
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-09-13 17:29:25
* @Entity com.itheima.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    List<InnerMarketDomain> getInnerMarketInfo(@Param("date") Date date, @Param("inner") List<String> inner);

    List<StockUpdownDomain> getStockAllPage(@Param("curDate") Date curDate);


    List<StockUpdownDomain> getStockIncrease(@Param("curDate") Date curDate);


    List<Map> getStockTradeAmt(@Param("openTime") Date openTime,@Param("endTime") Date endTime);

    int insertBatch(@Param("list") ArrayList<StockMarketIndexInfo> list);
}
