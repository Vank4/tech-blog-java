package com.techblog.domain.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(MultipartFile file);
}