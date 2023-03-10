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
        // ????????????????????????????????????
        List<User> list = list();
        // ????????????????????????????????????
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //?????????????????????
        writer.addHeaderAlias("username", "?????????");
        writer.addHeaderAlias("password", "??????");
        writer.addHeaderAlias("nickname", "??????");
        writer.addHeaderAlias("email", "??????");
        writer.addHeaderAlias("phone", "??????");
        writer.addHeaderAlias("address", "??????");
        writer.addHeaderAlias("createTime", "????????????");
        writer.addHeaderAlias("avatarUrl", "????????????");

        // ???????????????list???????????????excel??????????????????????????????????????????
        writer.write(list, true);

        // ??????????????????????????????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("????????????", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @Override
    public void excelImport(InputStream inputStream){
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //?????????????????????
        reader.addHeaderAlias("?????????", "username");
        reader.addHeaderAlias("??????", "password");
        reader.addHeaderAlias("??????", "nickname");
        reader.addHeaderAlias("??????", "email");
        reader.addHeaderAlias("??????", "phone");
        reader.addHeaderAlias("??????", "address");
        reader.addHeaderAlias("????????????", "createTime");
        reader.addHeaderAlias("????????????", "avatarUrl");

        List<User> users = reader.readAll(User.class);

        saveOrUpdateBatch(users);
    }

    @Override
    public Result login(UserLogin userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            String token = TokenUaits.GetToken(one.getId().toString(), one.getPassword());//??????token
            userDTO.setToken(token);//?????? token

            String roleFlag = one.getRole();//????????????
            List<Menu> roleMenus = getRoleMenus(roleFlag);//??????????????????
            userDTO.setMenus(roleMenus);

            return Result.ok(userDTO);
        } else {
            throw new ServiceException(Constants.CODE_600, "????????????????????????");
        }
    }

    @Override
    public Result register(UserLogin userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);  // ??? copy??????????????????????????????????????????
        } else {
            throw new ServiceException(Constants.CODE_600, "???????????????");
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
            throw new ServiceException(Constants.CODE_600, "???????????????");
        }
    }


    private User getUserInfo(UserLogin userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // ??????????????????????????????
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "????????????");
        }
        return one;
    }


    /**
     * ?????????????????????????????????
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = userRoleService.selectByFlag(roleFlag);
        // ???????????????????????????id??????
        List<Long> menuIds = roleMenuMapper.selectMenuId(roleId.longValue());

        // ???????????????????????????(??????)
        List<Menu> menus = menuService.selectAllmenu("");
        // new?????????????????????????????????list
        List<Menu> roleMenus = new ArrayList<>();
        // ?????????????????????????????????
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  ?????? children ???????????? menuIds???????????? ??????
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
