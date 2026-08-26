package com.onride.common.web.geo;

import com.uber.h3core.H3Core;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@AutoConfiguration
public class GeoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public H3Core h3Core() throws IOException {
        return H3Core.newInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public GeoIndex geoIndex(H3Core h3Core) {
        return new GeoIndex(h3Core);
    }
}