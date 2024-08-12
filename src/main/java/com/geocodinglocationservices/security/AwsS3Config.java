package com.geocodinglocationservices.security;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AwsS3Config {
    @Value("${aws.access_key_id}")
    private String awsAccessKeyId;

    @Value("${aws.secret_access_key}")
    private String awsSecretAccessKey;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket_name}")
    private String bucketName;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Bean
    public String bucketName() {
        return bucketName;
    }
}
