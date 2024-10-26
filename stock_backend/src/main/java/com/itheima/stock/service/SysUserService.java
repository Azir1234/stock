package com.itheima.stock.service;

import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;

import java.util.Map;

public interface SysUserService {
    SysUser selectByUsername(String username);


    R<LoginRespVo> loginB(LoginReqVo loginReqVo);

    R<Map> vCodeOut();
}
