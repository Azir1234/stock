package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domin.StockBlockDomain;
import com.itheima.stock.pojo.domin.StockBusinessDomain;
import com.itheima.stock.pojo.entity.StockBusiness;

import java.util.List;
import java.util.Map;

/**
* @author AZIR
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-09-13 17:29:25
* @Entity com.itheima.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    List<String> selectCodeList();

    List<Map> selectCodeNameList(String searchStr);

    StockBusinessDomain selectByCode(String code);
}
