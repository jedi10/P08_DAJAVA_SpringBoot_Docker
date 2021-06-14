package com.tourguide.gpsutils.config;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GpsUtilsBean {

    @Bean
    public GpsUtil gpsUtil()
    {
        return new GpsUtil();
    }
}
