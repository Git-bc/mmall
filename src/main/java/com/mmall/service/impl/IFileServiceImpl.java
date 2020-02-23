package com.mmall.service.impl;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.service.IFileService;
import com.mmall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service("iFileService")
public class IFileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(IFileService.class);

    @Value("${ftp.server.ip}")
    private String ip;
    @Value("${ftp.port}")
    private String port;
    @Value("${ftp.user}")
    private String username;
    @Value("${ftp.pass}")
    private String password;
    @Value("${ftp.basePath}")
    private String basePath;
    @Value("${ftp.filePath}")
    private String filePath;
    @Value("${ftp.server.http.prefix}")
    private String host;

    @Override
    public ServerResponse<Map> upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String uploadFileName = UUID.randomUUID().toString()+fileName.substring(fileName.lastIndexOf("."));
        try {
            System.out.println(port);
            boolean result = FtpUtil.uploadFile(ip, 21, username, password, basePath, filePath, uploadFileName,file.getInputStream());
            logger.info("开始上传文件，文件名：{"+fileName+"}，上传路径：{"+basePath+filePath+"},新文件名：{"+uploadFileName+"}");
            if (result){
                Map fileMap = Maps.newHashMap();
                fileMap.put("uri", uploadFileName);
                fileMap.put("url", host+filePath+uploadFileName);
                logger.info("上传文件成功");
                return ServerResponse.createBySuccess(fileMap);
            }
        } catch (IOException e) {
            logger.error("上传文件异常", e);
        }
        logger.info("上传文件失败");
        return ServerResponse.createByErrorMessage("上传文件失败");
    }

}
