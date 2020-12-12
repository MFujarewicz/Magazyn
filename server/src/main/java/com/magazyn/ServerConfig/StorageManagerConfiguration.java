package com.magazyn.ServerConfig;

import com.magazyn.Storage.StorageManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageManagerConfiguration {
    
    @Bean
    public StorageManager getDefaultStorageMenager() {
        return new StorageManager();
    }
}
