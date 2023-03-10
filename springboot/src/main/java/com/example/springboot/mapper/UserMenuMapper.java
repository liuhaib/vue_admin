package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.pojo.Menu;
import org.springframework.stereotype.Component;

@Component
public interface UserMenuMapper extends BaseMapper<Menu> {
}
