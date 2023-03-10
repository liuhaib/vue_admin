package com.example.springboot.controller;

import com.example.springboot.pojo.User;
import com.example.springboot.service.UserService;
import com.example.springboot.uaits.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result AddUser(@RequestBody User user){
        boolean save = userService.save(user);
        if(save){
            return Result.ok();
        }else{
            return Result.fail("新增失败");
        }
    }

    /**
     * 根据名字查询用户
     * @return
     */
    @GetMapping("/byname/{name}")
    public Result SelectByName(@PathVariable String name){
        return userService.getOneByName(name);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Result UpdateUser(@RequestBody User user){
        boolean saveOrUpdate = userService.saveOrUpdate(user);
        if(saveOrUpdate){
            return Result.ok();
        }else{
            return Result.fail("修改失败");
        }
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteUser(@PathVariable Long id){
        boolean removeById = userService.removeById(id);
        if(removeById){
            return Result.ok();
        }else{
            return Result.fail("删除失败");
        }
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        if(userService.removeByIds(ids)){
            return Result.ok();
        }else {
            return Result.fail("批量删除失败");
        }
    }

    /**
     * 多条件查询分页
     * @param pageNum
     * @param pageSize
     * @param username
     * @param email
     * @param address
     * @return
     */
    @GetMapping("/page")
    public Result SelectList(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             @RequestParam(defaultValue = "") String username,
                             @RequestParam(defaultValue = "") String email,
                             @RequestParam(defaultValue = "") String address){
        return  userService.SelectList(pageNum,pageSize,username,email,address);
    }

    /**
     * 用户数据 导出 excel
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/export")
    public Result Export(HttpServletResponse response) throws IOException {
        userService.excelExport(response);
        return Result.ok();
    }

    /**
     * 用户数据 导入 excel
     * @param file
     * @return
     */
    @PostMapping("/import")
    public Result Import(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            userService.excelImport(inputStream);
        }catch (IOException e){
            return Result.fail("导入失败");
        }
        return Result.ok();
    }



}
