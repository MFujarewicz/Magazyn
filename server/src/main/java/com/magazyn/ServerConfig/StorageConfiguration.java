package com.magazyn.ServerConfig;

import com.magazyn.Storage.IPathGenerator;
import com.magazyn.Storage.JobGenerator;
import com.magazyn.Storage.SimplePathGenerator;
import com.magazyn.Storage.StorageManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {
    
    @Bean
    public StorageManager getDefaultStorageMenager() {
        return new StorageManager();
    }

    @Bean
    public IPathGenerator getDefaultPathGenerator() {
        return new SimplePathGenerator();
    }

    @Bean
    public JobGenerator getDefaultJobGenerator() {
        return new JobGenerator();
    }
}