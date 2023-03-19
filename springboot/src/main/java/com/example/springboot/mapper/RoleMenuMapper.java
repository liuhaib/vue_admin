package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.pojo.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Delete("delete FROM sys_role_menu where roleid = #{roleId}")
    int DeleteByRoleId(@Param("roleId") Long roleId);

    @Select("select menuid FROM sys_role_menu where roleid = #{roleId}")
    List<Long> selectMenuId(@Param("roleId") Long roleId);

}