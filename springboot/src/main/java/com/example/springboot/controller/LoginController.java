package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.pojo.dto.UserLogin;
import com.example.springboot.service.UserService;
import com.example.springboot.uaits.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    UserService userService;

    /**
     * 用户登入
     * @param userlogin
     * @return
     */
    @PostMapping("/login")
    private Result Login(@RequestBody UserLogin userlogin){
        String username = userlogin.getUsername();
        String password = userlogin.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.fail("SB,用户名密码不能为空！");
        }
        return userService.login(userlogin);
    }

    /**
     * 用户注册
     * @param userlogin
     * @return
     */
    @PostMapping("/register")
    private Result Register (@RequestBody UserLogin userlogin){
        String username = userlogin.getUsername();
        String password = userlogin.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.fail("SB,用户名密码不能为空！");
        }
        return userService.register(userlogin);
    }

}
