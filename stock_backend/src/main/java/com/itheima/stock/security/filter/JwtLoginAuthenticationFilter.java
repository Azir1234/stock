package com.itheima.stock.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.itheima.stock.security.user.LoginUserDetail;
import com.itheima.stock.security.util.JwtTokenUtil;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.LoginRespVoExt;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author by itheima
 * @Date 2022/1/24
 * @Description 自定义登录拦截过滤器
 */
public class JwtLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private RedisTemplate redisTemplate;


    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 通过构造器传入指定的登录url地址
     * @param defaultFilterProcessesUrl 自定义url登录地址
     */
    public JwtLoginAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

       //从request域下获取ajax提交数据
        String userName=null;
        String password=null;
        //判断提供的内容格式
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ||
                request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            ServletInputStream in = request.getInputStream();
            //将流对象转化成Map
            LoginReqVo info = new ObjectMapper().readValue(in, LoginReqVo.class);
            //获取用户名和密码 我们约定，ajax登录时，用户名:userName,密码：password
            userName= (String) info.getUsername();
            password= (String) info.getPassword();


            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            String code= (String) redisTemplate.opsForValue().get(info.getSessionId());
            if(!info.getCode().equalsIgnoreCase((String) redisTemplate.opsForValue().get(info.getSessionId()))){
                R<Object> error=R.error(ResponseCode.CHECK_CODE_ERROR);
                String jsonData=new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
                return null;
            }
            if (userName==null || password==null) {
                throw new RuntimeException("用户名或者密码错误");
            }
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 认证成功处理方法
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        //获取用户的明细信息，包含用户名 密码（null） 权限集合
        LoginUserDetail principal = (LoginUserDetail) authResult.getPrincipal();
        String username=principal.getUsername();
        Collection<GrantedAuthority> authorities=principal.getAuthorities();
        String tokenStr=JwtTokenUtil.createToken(username,authorities.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        LoginRespVoExt respVo = new LoginRespVoExt();
        BeanUtils.copyProperties(principal,respVo);
        respVo.setAccessToken(tokenStr);
        R<LoginRespVoExt> ok=R.ok(respVo);
        response.getWriter().write(new ObjectMapper().writeValueAsString(ok));

    }

    /**
     * 认证失败后的处理方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setCharacterEncoding("UTF-8");

        R<Object> error = R.error(ResponseCode.ERROR);

        //响应数据
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
