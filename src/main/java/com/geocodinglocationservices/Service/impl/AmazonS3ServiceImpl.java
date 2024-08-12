package com.geocodinglocationservices.Service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.geocodinglocationservices.Service.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
@Service
public class AmazonS3ServiceImpl implements AmazonS3Service {
    @Autowired
    private AmazonS3 amazonS3;
    @Override
    public PutObjectResult upload(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });

        byte[] bytes = inputStream.readAllBytes();
        InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      //  log.debug("Path: " + path + ", FileName:" + fileName);
        return amazonS3.putObject(path, fileName, byteArrayInputStream, objectMetadata);
    }

    @Override
    public S3Object download(String path, String fileName) {
        return amazonS3.getObject(path, fileName);
    }
}
