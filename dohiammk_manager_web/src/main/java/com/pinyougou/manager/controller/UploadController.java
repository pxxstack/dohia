package com.pinyougou.manager.controller;

import com.pinyougou.FastDFSClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String file_server_url;




    @RequestMapping("/uploadImage")
    public Result uploadImage(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
//        获取文件的扩展名
        String exName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        try {
            //构建一个FastDFS上传客户端
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String path = client.uploadFile(file.getBytes(), exName);
            String url=file_server_url+path;
//            System.out.println(url);
            return new Result(true,url);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传图片失败");
        }

    }
}
