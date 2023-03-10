package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.pojo.Menu;
import com.example.springboot.uaits.Result;

public interface UserMenuService extends IService<Menu> {
    Result SelectMenuList(String name);
}
