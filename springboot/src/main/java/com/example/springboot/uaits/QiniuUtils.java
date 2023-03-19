package com.example.springboot.uaits;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.net.URLEncoder;

@Component
public class QiniuUtils {

    @Resource
    private UploadManager uploadManager;
    @Resource
    private Auth auth;

    @Value("${qiniu.bucketName}")
    private String bucketName;
    @Value("${qiniu.path}")
    private String url;

    public String upload(FileInputStream file, String fileName) throws QiniuException {
        String token = auth.uploadToken(bucketName);
        Response res = uploadManager.put(file, fileName, token, null, null);
        if (!res.isOK()) {
            throw new RuntimeException("上传七牛云出错:" + res);
        }
        return url + "/" + fileName;
    }

    /**
     * 获取公共空间文件
     * @param filename
     * @return
     */
    public String getFile(String filename) throws Exception{
        String xz = "?attname=";
        String encodedFileName = URLEncoder.encode(filename, "utf-8").replace("+", "%20");
        String xzurl = String.format("%s/%s", url, encodedFileName);
        return xzurl+xz+filename;
    }


}
