package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.pojo.User;
import com.example.springboot.pojo.dto.UserLogin;
import com.example.springboot.uaits.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public interface UserService extends IService<User> {

    Result SelectList(Integer pageNum, Integer pageSize, String username, String email, String address);

    void excelExport(HttpServletResponse response) throws IOException;

    void excelImport(InputStream inputStream);

    Result login(UserLogin userlogin);

    Result register(UserLogin userlogin);

    Result getOneByName(String name);
}
