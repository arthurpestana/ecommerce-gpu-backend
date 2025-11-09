package org.acme.services.storage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.io.InputStream;
import java.time.Duration;

@ApplicationScoped
public class StorageServiceImpl implements StorageService {

    @Inject
    S3Client s3Client;

    @Inject
    S3Presigner presigner;

    @ConfigProperty(name = "app.s3.bucket")
    String bucket;

    @ConfigProperty(name = "quarkus.s3.endpoint-override")
    String endpoint;

    @Override
    public void ensureBucketExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        }
    }

    @Override
    public String uploadFile(String objectName, InputStream fileStream, String contentType, Long size) {
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .contentType(contentType)
                .build();

        s3Client.putObject(req, RequestBody.fromInputStream(fileStream, size));

        return publicUrl(objectName);
    }

    @Override
    public String uploadBytes(String objectName, byte[] bytes, String contentType) {
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .contentType(contentType)
                .build();

        s3Client.putObject(req, RequestBody.fromBytes(bytes));

        return publicUrl(objectName);
    }

    @Override
    public InputStream downloadFile(String objectName) {
        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .build();

        return s3Client.getObject(req);
    }

    @Override
    public void deleteFile(String objectName) {
        DeleteObjectRequest req = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .build();

        s3Client.deleteObject(req);
    }

    @Override
    public String publicUrl(String objectName) {
        return "%s/%s/%s".formatted(endpoint, bucket, objectName);
    }

    @Override
    public String presignedGetObject(String objectName, Integer expiration) {
        GetObjectRequest req = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .build();

        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expiration))
                .getObjectRequest(req)
                .build();

        return presigner.presignGetObject(presignReq).url().toString();
    }

    @Override
    public String presignedPutObject(String objectName, Integer expiration) {
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .build();

        PutObjectPresignRequest presignReq = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expiration))
                .putObjectRequest(req)
                .build();

        return presigner.presignPutObject(presignReq).url().toString();
    }
}
