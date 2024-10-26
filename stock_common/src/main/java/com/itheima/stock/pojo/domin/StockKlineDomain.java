package com.itheima.stock.pojo.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockKlineDomain {

    private BigDecimal avgPrice;

    private BigDecimal minPrice;

    private BigDecimal openPrice;

    private BigDecimal maxPrice;

    private BigDecimal closePrice;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date mxTime;

    private String stockCode;
}
