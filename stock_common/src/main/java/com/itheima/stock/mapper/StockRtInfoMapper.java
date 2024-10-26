package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domin.Stock4EvrDayDomain;
import com.itheima.stock.pojo.domin.Stock4MinuteDomain;
import com.itheima.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author AZIR
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-09-13 17:29:25
* @Entity com.itheima.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    List<Map> getStockUpDownCount(@Param("flag") int flag, @Param("openTime") Date openTime, @Param("curTime") Date curTime);

    List<Map> getStockUpdown(@Param("curTime") Date curTime);

    List<Stock4MinuteDomain> getStockTimeSharing(@Param("code") String code, @Param("openTime") Date openTime, @Param("endTime") Date endTime);

    List<Stock4EvrDayDomain> getStockScreenDkline(@Param("code") String code, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    int insertBatch(List<StockRtInfo> stockRtInfos);



}
