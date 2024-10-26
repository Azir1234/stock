package com.itheima.stock.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class PermissionRespNodeVo {

    /**
     * 角色ID
     */
    private String id;
    /**
     * 角色标题
     */
    private String title;
    /**
     * 角色图标
     */
    private String icon;
    /**
     * 路由地址URL
     */
    private String path;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 菜单数结构
     */
    private List<PermissionRespNodeVo> children;

}
