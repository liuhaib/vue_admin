package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.pojo.Menu;
import com.example.springboot.pojo.User;
import com.example.springboot.pojo.dto.UserLogin;
import com.example.springboot.service.UserMenuService;
import com.example.springboot.service.UserRoleService;
import com.example.springboot.service.UserService;
import com.example.springboot.uaits.Constants;
import com.example.springboot.uaits.Result;
import com.example.springboot.uaits.TokenUaits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Autowired
    UserMenuServiceImpl menuService;

    @Override
    public Result SelectList(Integer pageNum, Integer pageSize, String username, String email, String address) {
        Page<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            wrapper.like("username", username);
        }
        if (!"".equals(email)) {
            wrapper.like("email", email);
        }
        if (!"".equals(address)) {
            wrapper.like("address", address);
        }
        wrapper.orderByDesc("id");
        Page<User> userPage = page(page, wrapper);
        return Result.ok(userPage);
    }

    @Override
    public void excelExport(HttpServletResponse response) throws IOException {
        // 从数据库查询出所有的数据
        List<User> list = list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像地址");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @Override
    public void excelImport(InputStream inputStream){
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //自定义标题别名
        reader.addHeaderAlias("用户名", "username");
        reader.addHeaderAlias("密码", "password");
        reader.addHeaderAlias("昵称", "nickname");
        reader.addHeaderAlias("邮箱", "email");
        reader.addHeaderAlias("电话", "phone");
        reader.addHeaderAlias("地址", "address");
        reader.addHeaderAlias("创建时间", "createTime");
        reader.addHeaderAlias("头像地址", "avatarUrl");

        List<User> users = reader.readAll(User.class);

        saveOrUpdateBatch(users);
    }

    @Override
    public Result login(UserLogin userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            String token = TokenUaits.GetToken(one.getId().toString(), one.getPassword());//创建token
            userDTO.setToken(token);//签发 token

            String roleFlag = one.getRole();//获取权限
            List<Menu> roleMenus = getRoleMenus(roleFlag);//获取权限菜单
            userDTO.setMenus(roleMenus);

            return Result.ok(userDTO);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public Result register(UserLogin userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return Result.ok();
    }

    @Override
    public Result getOneByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", name);
        User one = getOne(queryWrapper);
        if (one != null) {
           return Result.ok(one);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户不存在");
        }
    }


    private User getUserInfo(UserLogin userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }


    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = userRoleService.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Long> menuIds = roleMenuMapper.selectMenuId(roleId.longValue());

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.selectAllmenu("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
