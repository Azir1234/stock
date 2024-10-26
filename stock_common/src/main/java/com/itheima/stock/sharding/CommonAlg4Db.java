package com.itheima.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommonAlg4Db implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
    /**
     * 精准查询时走的方法，cur_time 匹配 =或 in
     * @param dsNames ds-2021,ds-2023,...
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> dsNames, PreciseShardingValue<Date> shardingValue) {
        String logicTableName=shardingValue.getLogicTableName();
        String columnName=shardingValue.getColumnName();
        Date curTime=shardingValue.getValue();
        String year=new DateTime(curTime).getYear()+"";
        Optional<String> result = dsNames.stream().filter(dsName -> dsName.endsWith(year)).findFirst();
        if(result.isPresent()){
            return result.get();
        }
        return null;
    }

    /**
     * 模糊查询分库
     * @param dsNames
     * @param shardingValue
     * @return
     */

    @Override
    public Collection<String> doSharding(Collection<String> dsNames, RangeShardingValue<Date> shardingValue) {
        //获取逻辑表
        String logicTableName=shardingValue.getLogicTableName();
        //分片键
        String columnName=shardingValue.getColumnName();
        Range<Date> valueRange = shardingValue.getValueRange();
        if(valueRange.hasLowerBound()){
            Date startTime=valueRange.lowerEndpoint();
            int startYear=new DateTime(startTime).getYear();
            dsNames= dsNames.stream().filter(dsName->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-")+1))>=startYear).collect(Collectors.toList());
        }
        if(valueRange.hasUpperBound()){
            Date endTime=valueRange.upperEndpoint();
            int endYear=new DateTime(endTime).getYear();
            dsNames= dsNames.stream().filter(dsName->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-")+1))<=endYear ).collect(Collectors.toList());
        }
        return dsNames;

    }
}
