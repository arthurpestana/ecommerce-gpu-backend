package org.acme.services.storage;

import java.io.InputStream;

public interface StorageService {
    
    void ensureBucketExists();

    String uploadFile(String objectName, InputStream fileStream, String contentType, Long size);

    String uploadBytes(String objectName, byte[] bytes, String contentType);

    InputStream downloadFile(String objectName);

    void deleteFile(String objectName);

    String publicUrl(String objectName);

    String presignedGetObject(String objectName, Integer expiration);

    String presignedPutObject(String objectName, Integer expiration);
}
