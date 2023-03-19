package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.pojo.Role;
import org.springframework.stereotype.Component;

@Component
public interface UserRoleMapper extends BaseMapper<Role> {
}
