package com.sccm.services.implementation;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.sccm.helpers.AppConstants;
import com.sccm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService{

    private Cloudinary cloudinary;

    //Instead of autowired, constructor automatically injects the data
    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }    

    @Override
    public String uploadImage(MultipartFile contactImg, String filename) {
        //upload image

        try {
            byte[] data = new byte[contactImg.getInputStream().available()];
            contactImg.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", filename
            ));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return this.getUrlFromPublicId(filename);
    }

    @Override
    public String getUrlFromPublicId(String publicId){

        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
                .width(AppConstants.CONTACT_IMG_WIDTH)
                .height(AppConstants.CONTACT_IMG_HEIGHT)
                .crop(AppConstants.CONTACT_IMG_CROP)
        )
        .generate(publicId);
    }
}
