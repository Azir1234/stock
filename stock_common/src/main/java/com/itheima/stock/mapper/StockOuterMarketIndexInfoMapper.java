package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domin.SOMIInfoDomain;
import com.itheima.stock.pojo.entity.StockOuterMarketIndexInfo;

import java.util.List;

/**
* @author AZIR
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2024-09-13 17:29:25
* @Entity com.itheima.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    List<SOMIInfoDomain> getStockOutMarketIndex();
}
