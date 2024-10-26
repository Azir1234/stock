package com.itheima.stock.pojo.domin;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
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
public class SOMIInfoDomain {


    private String name;
    private BigDecimal curPoint;
    private BigDecimal upDown;
    private BigDecimal rose;
    @JsonFormat(pattern = "yyyyMMdd")
    private Date curTime;
}
