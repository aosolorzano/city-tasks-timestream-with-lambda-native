package com.hiperium.city.tasks.events.configurations;

import com.hiperium.city.tasks.events.utils.FunctionsUtil;
import com.hiperium.city.tasks.events.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;
import java.util.Objects;

@Slf4j
@Configuration
public class DynamoDBClientConfig {

    private final Environment environment;

    public DynamoDBClientConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {
        var builder = DynamoDbAsyncClient.builder()
                .region(DefaultAwsRegionProviderChain.builder().build().getRegion())
                .credentialsProvider(DefaultCredentialsProvider.builder().build());
        String endpointOverrideURL = this.environment.getProperty(PropertiesUtil.AWS_ENDPOINT_OVERRIDE_PROPERTY);
        if (Objects.nonNull(endpointOverrideURL) && !endpointOverrideURL.isBlank()) {
            log.debug("DynamoDB Endpoint Override: {}", endpointOverrideURL);
            builder.endpointOverride(URI.create(endpointOverrideURL));
        }
        return builder.build();
    }
}
