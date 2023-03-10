package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.pojo.UserFile;
import com.example.springboot.uaits.Result;

import java.util.List;

public interface UserFileService extends IService<UserFile> {

    Result ListPage(Integer pageNum, Integer pageSize, String name);

    Result DeleteId(Integer id);

    Result DeleteBatch(List<Integer> ids);
}
