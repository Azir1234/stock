package com.itheima.stock.service.impl;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.itheima.stock.pojo.constant.StockConstant;
import com.itheima.stock.service.SysUserService;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private IdWorker idWorker;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser selectByUsername(String username){


        SysUser sysUser= sysUserMapper.selectByUsername(username);
        return  sysUser;
    }

    @Override
    public R<LoginRespVo> loginB(LoginReqVo loginReqVo) {
        String username = loginReqVo.getUsername();
        String password=loginReqVo.getPassword();
        String code=loginReqVo.getCode();
        String sessionId=loginReqVo.getSessionId();

        //判断数据
        if(loginReqVo==null||username==null||password==null||code==null){

            return R.error(ResponseCode.DATA_ERROR);
        }
        if(!code.equalsIgnoreCase((String) redisTemplate.opsForValue().get(sessionId))){
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        SysUser sysUser=sysUserMapper.selectByUsername(username);
        if(sysUser==null||!passwordEncoder.matches(password ,sysUser.getPassword() )){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }



        LoginRespVo respVo=new LoginRespVo();
        BeanUtils.copyProperties(sysUser,respVo);


        return R.ok(respVo);
    }

    @Override
    public R<Map> vCodeOut() {

        //sessionID
        String sessionId = String.valueOf(idWorker.nextId());
        //图片Code和图片image
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        captcha.setBackground(Color.lightGray);
        String checkCode = captcha.getCode();
        String image64=captcha.getImageBase64();
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX +sessionId,checkCode,5, TimeUnit.MINUTES);
        log.info("sessionId:{},checkCode:{}",sessionId,checkCode);
        Map<String,String> map=new HashMap<>();
        map.put("imageData",image64);
        map.put("sessionId",StockConstant.CHECK_PREFIX+sessionId);
        return R.ok(map);
    }
}
