package org.acme.services.storage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@ApplicationScoped
public class S3ClientProducer {

    @ConfigProperty(name = "quarkus.s3.endpoint-override")
    String endpoint;

    @ConfigProperty(name = "quarkus.s3.aws.region")
    String region;

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id")
    String accessKey;

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key")
    String secret;

    @Produces
    @Singleton
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secret)
                        )
                )
                .region(Region.of(region))
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
    }

    @Produces
    @Singleton
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secret)
                        )
                )
                .region(Region.of(region))
                .build();
    }
}
