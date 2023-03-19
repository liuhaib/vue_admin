package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.mapper.UserFileMapper;
import com.example.springboot.pojo.UserFile;
import com.example.springboot.service.UserFileService;
import com.example.springboot.uaits.QiniuUtils;
import com.example.springboot.uaits.Result;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private QiniuUtils qiniuUtils;

    @Resource
    UserFileService userFileService;

    /**
     * 实现 oss 上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload01(@RequestParam MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return "上传文件为空,请重试...";
        }

        //获取上传 文件 名称
        String originalFilename = file.getOriginalFilename();
        //获取上传 文件 类型
        String type = FileUtil.extName(originalFilename);
        //获取上传 文件 的大小
        long size = file.getSize();


        // 定义一个文件唯一的标识码
        String ThisTime = LocalDate.now().toString();//获取当天日期
        String uuid = IdUtil.fastSimpleUUID();//唯一标识
        String fileName = ThisTime + StrUtil.UNDERLINE + uuid +StrUtil.DOT + type;


        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        UserFile dbFiles = getFileByMd5(md5);

        try {

            if (dbFiles != null) {
                return dbFiles.getUrl();
            }else {
                FileInputStream uploadFile = (FileInputStream) file.getInputStream();
                String path = qiniuUtils.upload(uploadFile, fileName);
                url = "http://" + path;
                //写入数据库
                UserFile userFile = new UserFile();
                userFile.setSize(size/1024);
                userFile.setType(type);
                userFile.setName(fileName);
                userFile.setUrl(url);
                userFile.setMd5(md5);
                userFileService.save(userFile);
                return url;
            }
        } catch (IOException e) {
             e.printStackTrace();
            return "服务器内部错误...";
        }


    }

    /**
     * 实现oss下载
     * @param filename
     * @param response
     * @throws IOException
     */
    @GetMapping("/{filename}")
    public Result  download(@PathVariable String filename, HttpServletResponse response) throws Exception {
        if (filename.isEmpty()) {
            return Result.fail("文件名为空");
        }
        try {
            String privateFile = qiniuUtils.getFile(filename);
            return Result.ok(privateFile);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("系统错误");
        }
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String name) {
        return userFileService.ListPage(pageNum,pageSize,name);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return userFileService.DeleteId(id);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return userFileService.DeleteBatch(ids);
    }

    @PostMapping("/update")
    public Result update(@RequestBody UserFile files) {
        return Result.ok(userFileService.updateById(files));
    }

    private UserFile getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<UserFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<UserFile> userFiles = userFileService.list(queryWrapper);
        return userFiles.size() == 0 ? null : userFiles.get(0);
    }
}
