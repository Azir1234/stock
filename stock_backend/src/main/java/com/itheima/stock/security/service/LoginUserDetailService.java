package com.itheima.stock.security.service;

import com.alibaba.excel.util.StringUtils;
import com.itheima.stock.mapper.SysRoleMapper;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.entity.SysPermission;
import com.itheima.stock.pojo.entity.SysRole;
import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.security.user.LoginUserDetail;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;

import org.assertj.core.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class LoginUserDetailService implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;
   @Autowired
   private PermissionService permissionService;

   @Autowired
   private SysRoleMapper sysRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser dbUser= sysUserMapper.selectByUsername(username);
        if(dbUser==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        //组装userdetail对象
        List<SysPermission> permissions=permissionService.getPermissionByUserId(dbUser.getId());
        List<PermissionRespNodeVo> menus=permissionService.getTree(permissions,"0",true);
        List<SysRole> roles = sysRoleMapper.getRoleByUserId(dbUser.getId());
        List<String> authBtnPerms=permissions.stream().filter(per->!Strings.isNullOrEmpty(per.getCode())&&per.getType()==3)
                .map(per->per.getCode()).collect(Collectors.toList());


        ArrayList<String> ps=new ArrayList<>();
        List<String> pers=  permissions.stream().
                filter(per-> StringUtils.isNotBlank(per.getPerms())).map(per->per.getPerms()).collect(Collectors.toList());
        ps.addAll(pers);
        List<String> rs = roles.stream().map(r -> "ROLE_" + r.getName()).collect(Collectors.toList());
        ps.addAll(rs);
        String[] psArray=ps.toArray(new String[pers.size()]);
        List<GrantedAuthority> authorityList= AuthorityUtils.createAuthorityList(psArray);

        LoginUserDetail userDetail=new LoginUserDetail();
        BeanUtils.copyProperties(dbUser,userDetail);
        userDetail.setId(dbUser.getId().toString());
        userDetail.setMenus(menus);
        userDetail.setPermissions(authBtnPerms);
        userDetail.setAuthorities(authorityList);
        return userDetail;
    }
}
