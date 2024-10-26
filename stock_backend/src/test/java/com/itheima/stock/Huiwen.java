package com.itheima.stock;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.itheima.stock.utils.DateTimeUtil;
import org.joda.time.DateTime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;

public class Huiwen {

    public static void main(String[] args) {
        // 输入日期
        String inputDate = "2024-10-12"; // 格式为 yyyy-MM-dd
        LocalDate date = LocalDate.parse(inputDate);

        System.out.println(date);
        // 获取星期几
        int dayOfWeek= date.getDayOfWeek().getValue();
        System.out.println("日期 " + inputDate + " 是星期 " + dayOfWeek);
        DateTime curTime=DateTime.now();
        if(DateTimeUtil.isWorkDay(curTime)){

        }else{
            //非工作日
            //最后交易时间点
           DateTime PrevioustradingDay=DateTimeUtil.getPreviousTradingDay(curTime);
            System.out.println(PrevioustradingDay);
        }

    }



}
