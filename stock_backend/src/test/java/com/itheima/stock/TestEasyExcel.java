package com.itheima.stock;

import com.alibaba.excel.EasyExcel;
import com.itheima.stock.pojo.User;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/31
 * @Description
 */
public class TestEasyExcel {

    public List<User> init(){
        //组装数据
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAddress("上海"+i);
            user.setUserName("张三"+i);
            user.setBirthday(new Date());
            user.setAge(10+i);
            users.add(user);
        }
        return users;
    }
    /**
     * 直接导出后，表头名称默认是实体类中的属性名称
     */
    @Test
    public void test02() throws IOException {
        List<User> users = init();

        File file=new File("C:\\Users\\AZIR\\Desktop\\ex\\用户.xls");
        File parentFile = file.getParentFile();
        System.out.println(parentFile);
        parentFile.mkdirs();
        file.createNewFile();

        // file.mkdir();
        //file.createNewFile();
        //不做任何注解处理时，表头名称与实体类属性名称一致
        EasyExcel.write("C:\\Users\\AZIR\\Desktop\\ex\\用户.xls",User.class)
                .sheet("用户信息").doWrite(users);

    }
}