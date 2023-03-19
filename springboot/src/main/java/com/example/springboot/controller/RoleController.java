package com.example.springboot.controller;

import com.example.springboot.pojo.Role;
import com.example.springboot.service.UserRoleService;
import com.example.springboot.uaits.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    UserRoleService userRoleService;


    @GetMapping("/page")
    public Result SelectList(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             @RequestParam(defaultValue = "") String name){
        return  userRoleService.SelectRoleList(pageNum,pageSize,name);
    }



    @PostMapping("/add")
    public Result AddUser(@RequestBody Role role){
        boolean save = userRoleService.save(role);
        if(save){
            return Result.ok();
        }else{
            return Result.fail("新增失败");
        }
    }

    @PostMapping("/update")
    public Result UpdateUser(@RequestBody Role role){
        boolean saveOrUpdate = userRoleService.saveOrUpdate(role);
        if(saveOrUpdate){
            return Result.ok();
        }else{
            return Result.fail("修改失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteUser(@PathVariable Long id){
        boolean removeById = userRoleService.removeById(id);
        if(removeById){
            return Result.ok();
        }else{
            return Result.fail("删除失败");
        }
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        if(userRoleService.removeByIds(ids)){
            return Result.ok();
        }else {
            return Result.fail("批量删除失败");
        }
    }
    @PostMapping("/roleMenu/{roleId}")
    public Result SetRoleMenu(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
      return userRoleService.setRoleMenu(roleId,menuIds);
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result GetSetRoleMenu(@PathVariable Long roleId) {
        return userRoleService.getRoleMenu(roleId);
    }

    @GetMapping("/getAll")
    public Result GetAllMenu() {
        return Result.ok(userRoleService.list());
    }
}
