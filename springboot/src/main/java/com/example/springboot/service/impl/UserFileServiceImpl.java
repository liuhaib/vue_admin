package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.mapper.UserFileMapper;
import com.example.springboot.pojo.UserFile;
import com.example.springboot.service.UserFileService;
import com.example.springboot.uaits.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

    @Override
    public Result ListPage(Integer pageNum, Integer pageSize, String name) {
        QueryWrapper<UserFile> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        Page<UserFile> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.ok(page);
    }

    @Override
    public Result DeleteId(Integer id) {
        UserFile userfile = getById(id);
        if(userfile!=null){
            userfile.setIsDelete(true);
            saveOrUpdate(userfile);
            return Result.ok();
        }
        return Result.fail("所删除的文件不存在！");
    }

    @Override
    public Result DeleteBatch(List<Integer> ids) {
        QueryWrapper<UserFile> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<UserFile> userFileList = list(queryWrapper);
        for (UserFile file : userFileList) {
            file.setIsDelete(true);
            updateById(file);
        }
        return Result.ok();
    }
}
