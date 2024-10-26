package com.itheima.stock.controller;

import com.itheima.stock.service.SysUserService;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apiguardian.api.API;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(tags = "用户相关接口处理器")
public class SysUserController {
    /*@Value("${appName}")
    private String appName;*/
    @Autowired
    private SysUserService sysUserService;
    @ApiOperation(value = "根据用户名查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "用户名",dataType = "string",required = true,type = "path")
    })
    @GetMapping("/user/{username}")
    public SysUser selectByUsername(@PathVariable("username") String username){

        SysUser sysUser= sysUserService.selectByUsername(username);
        return sysUser;

    }

    @PostMapping("/login")
    public R<LoginRespVo> loginB(@RequestBody LoginReqVo vo){
        return   sysUserService.loginB(vo);

    }
    @GetMapping("/captcha")
    public R<Map> captcha(){
        return sysUserService.vCodeOut();
    }


}