package com.sccm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile contactImg, String filename);

    String getUrlFromPublicId(String publicId);

}
