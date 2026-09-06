package com.onride.observability.grpc;

import io.grpc.ClientInterceptor;
import io.grpc.ServerInterceptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(ClientInterceptor.class)
public class GrpcTracingAutoConfiguration {

    @Bean
    @GrpcGlobalClientInterceptor
    public ClientInterceptor grpcTracingClientInterceptor(OpenTelemetry openTelemetry) {
        return GrpcTelemetry.create(openTelemetry).createClientInterceptor();
    }

    @Bean
    @GrpcGlobalServerInterceptor
    public ServerInterceptor grpcTracingServerInterceptor(OpenTelemetry openTelemetry) {
        return GrpcTelemetry.create(openTelemetry).createServerInterceptor();
    }
}