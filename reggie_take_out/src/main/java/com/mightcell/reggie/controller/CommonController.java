package com.mightcell.reggie.controller;

import com.mightcell.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件的上传与下载（通用）
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return 成功响应
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
//        file是一个临时文件，需要转存到合适的位置，否则本次请求完成之后，临时文件会被删除、

//        动态获取原始文件的后缀
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        利用UUID随机生成文件名，防止文件名称重复，造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

//        创建目录对象
        File dir = new File(basePath);
//        判断当前目录是否存在
        if (!dir.exists()) {
//            目录不存在，需要创建
            dir.mkdir();
        } 

//        文件转存
        try {
//            将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }
}
