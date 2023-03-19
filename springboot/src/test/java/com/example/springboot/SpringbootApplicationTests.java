package com.example.springboot;

import com.example.springboot.mapper.DictMapper;
import com.example.springboot.pojo.User;
import com.example.springboot.service.UserMenuService;
import com.example.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    DictMapper dictMapper;
    @Autowired
    UserMenuService userMenuService;



    @Test
    void contextLoads() {
        List<User> list = userService.list();
        for (User user : list) {
            System.out.println(user);
        }
    }


}
