package com.itheima.stock.service.impl;


import com.google.common.collect.Lists;
import com.itheima.stock.mapper.SysPermissionMapper;
import com.itheima.stock.pojo.entity.SysPermission;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 根据用户id查询用户信息
     * @param userId
    eturn
     */
    @Override
    public List<SysPermission> getPermissionByUserId(Long userId) {
        return sysPermissionMapper.getPermissionByUserId(userId);
    }

    /**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    @Override
    public List<PermissionRespNodeVo> getTree(List<SysPermission> permissions, String  pid, boolean isOnlyMenuType) {
        ArrayList<PermissionRespNodeVo> list = Lists.newArrayList();
        //如果为空
        // if (CollectionUtils.isEmpty(permissions)) {
        //     return list;
        // }
        //遍历权限树状集合
        for (SysPermission permission : permissions) {
            //如果权限父id相等
            if (permission.getPid().toString().equals(pid)) {
                if (permission.getType().intValue()!=3 || !isOnlyMenuType) {
                    PermissionRespNodeVo respNodeVo = new PermissionRespNodeVo();
                    respNodeVo.setId(permission.getId().toString());
                    respNodeVo.setTitle(permission.getTitle());
                    respNodeVo.setIcon(permission.getIcon());
                    respNodeVo.setPath(permission.getUrl());
                    respNodeVo.setName(permission.getName());
                    respNodeVo.setChildren(getTree(permissions,permission.getId().toString(),isOnlyMenuType));
                    list.add(respNodeVo);
                }
            }
        }
        return list;
    }
}
