package com.example.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserRoleMapper;
import com.example.springboot.pojo.Menu;
import com.example.springboot.pojo.Role;
import com.example.springboot.pojo.RoleMenu;
import com.example.springboot.service.UserMenuService;
import com.example.springboot.service.UserRoleService;
import com.example.springboot.uaits.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, Role> implements UserRoleService {

    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Autowired
    UserMenuService menuService;

    @Override
    public Result SelectRoleList(Integer pageNum, Integer pageSize, String name) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        Page<Role> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.ok(page);
    }


    @Transactional
    @Override
    public Result setRoleMenu(Long roleId, List<Long> menuIds) {
        //先删除 当前 角色 的菜单id
        roleMenuMapper.DeleteByRoleId(roleId);

        List<Long> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Long menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) { // 二级菜单 并且传过来的menuId数组里面没有它的父级id
                // 那么我们就得补上这个父级id
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleid(roleId);
                roleMenu.setMenuid(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleid(roleId);
            roleMenu.setMenuid(menuId);
            roleMenuMapper.insert(roleMenu);
        }
      return Result.ok();
    }

    @Override
    public Result getRoleMenu(Long roleId) {
        List<Long> roleMenus = roleMenuMapper.selectMenuId(roleId);
        return Result.ok(roleMenus);
    }

    @Override
    public Integer selectByFlag(String roleFlag) {
      QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("flag",roleFlag);
        Role one = getOne(queryWrapper);
        Long id = one.getId();
        return id.intValue();
    }
}
