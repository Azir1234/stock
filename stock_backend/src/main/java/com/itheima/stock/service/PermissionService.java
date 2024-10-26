package com.itheima.stock.service;

import com.itheima.stock.pojo.entity.SysPermission;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionService {
    /**
     * 根据用户id查询用户信息
    aram userId
     * @return
     */
    List<SysPermission> getPermissionByUserId(@Param("userId") Long userId);

    /**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    List<PermissionRespNodeVo> getTree(List<SysPermission> permissions, String pid, boolean isOnlyMenuType);
}