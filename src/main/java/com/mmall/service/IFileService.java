package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by geely
 */
public interface IFileService {

    ServerResponse<Map> upload(MultipartFile file);
}
