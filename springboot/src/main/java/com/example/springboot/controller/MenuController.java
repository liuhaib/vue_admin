package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.mapper.DictMapper;
import com.example.springboot.pojo.Dict;
import com.example.springboot.pojo.Menu;
import com.example.springboot.service.UserMenuService;
import com.example.springboot.uaits.Constants;
import com.example.springboot.uaits.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    UserMenuService userMenuService;

    @Autowired
    DictMapper dictMapper;


    @GetMapping("/all")
    public Result SelectList(@RequestParam(defaultValue = "") String name){
        return  userMenuService.SelectMenuList(name);
    }

    @PostMapping("/add")
    public Result AddUser(@RequestBody Menu menu){
        boolean save = userMenuService.save(menu);
        if(save){
            return Result.ok();
        }else{
            return Result.fail("新增失败");
        }
    }

    @PostMapping("/update")
    public Result UpdateUser(@RequestBody Menu menu){
        boolean saveOrUpdate = userMenuService.saveOrUpdate(menu);
        if(saveOrUpdate){
            return Result.ok();
        }else{
            return Result.fail("修改失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteUser(@PathVariable Long id){
        boolean removeById = userMenuService.removeById(id);
        if(removeById){
            return Result.ok();
        }else{
            return Result.fail("删除失败");
        }
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        if(userMenuService.removeByIds(ids)){
            return Result.ok();
        }else {
            return Result.fail("批量删除失败");
        }
    }

    @GetMapping("/typeIcon")
    public Result SelectDictList(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "icon");
        return Result.ok(dictMapper.selectList(queryWrapper));
    }

    @GetMapping("/ids")
    public Result SelectMenuIdList(){
        //技术点  使用 lambda 表达式 的 .map获取id
        return Result.ok(userMenuService.list().stream().map(Menu::getId));
    }
}
