package com.itheima.stock.mapper;



import com.itheima.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.FQJ.stock.pojo.entity.SysPermission
 */
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

	/**
	 * 获取集合
	 * @param userId
	 * @return
	 */
	List<SysPermission> getPermissionByUserId(@Param("userId") Long userId);
}
