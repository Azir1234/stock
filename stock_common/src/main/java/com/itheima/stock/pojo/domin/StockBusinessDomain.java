package com.itheima.stock.pojo.domin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockBusinessDomain {
    private String code;

    /**
     * 股票名称
     */
    private String name;



    /**
     * 行业板块名称
     */
    private String trade;

    /**
     * 主营业务
     */
    private String business;


}
