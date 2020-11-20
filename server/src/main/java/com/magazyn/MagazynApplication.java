package com.example.magazyn;

import com.example.magazyn.database.Jobs;
import com.example.magazyn.database.Product;
import com.example.magazyn.database.repositories.JobsRepository;
import com.example.magazyn.database.repositories.ProductRepository;
import com.example.magazyn.database.Type;
import com.example.magazyn.database.repositories.TypeRepository;
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
    public CommandLineRunner demo(ProductRepository repo1 , JobsRepository repo2) throws Exception {
        return (args) -> {
//            Type type = new Type("typ");
            Jobs jobs = new Jobs();
//            Product p = new Product("p1", 5, type);
            repo2.save(jobs);
//            repo1.save(p);
        };
    }

}
