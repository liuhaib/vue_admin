package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.pojo.Role;
import com.example.springboot.uaits.Result;

import java.util.List;

public interface UserRoleService extends IService<Role> {
    Result SelectRoleList(Integer pageNum, Integer pageSize, String name);

    Result setRoleMenu(Long roleId, List<Long> menuIds);

    Result getRoleMenu(Long roleId);

    Integer selectByFlag(String roleFlag);
}
