package com.magazyn;

import com.magazyn.database.Job;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MagazynApplication {

    public static void main(String[] args) {
        SpringApplication.run(MagazynApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(ProductDataRepository repo1 , JobRepository repo2) throws Exception {
        return (args) -> {
//            Type type = new Type("typ");
//            Job job = new Job();
//            Product p = new Product("p1", 5, type);
//            repo2.save(job);
//            repo1.save(p);
        };
    }

}
