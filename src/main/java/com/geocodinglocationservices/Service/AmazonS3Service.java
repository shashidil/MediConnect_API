package com.geocodinglocationservices.Service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface AmazonS3Service {
    public PutObjectResult upload(
            String path,
            String fileName,
            Optional<Map<String, String>> optionalMetaData,
            InputStream inputStream) throws IOException;

    public S3Object download(String path, String fileName);
}
