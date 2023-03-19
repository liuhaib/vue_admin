package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.pojo.User;
import org.springframework.stereotype.Component;


@Component
public interface UserMapper extends BaseMapper<User> {
}
