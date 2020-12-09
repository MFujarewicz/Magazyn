package com.magazyn;

import com.magazyn.database.Job;
import com.magazyn.database.ProductData;
import com.magazyn.database.Type;
import com.magazyn.database.repositories.JobRepository;
import com.magazyn.database.repositories.ProductDataRepository;
import com.magazyn.database.repositories.TypeRepository;
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
    public CommandLineRunner demo(ProductDataRepository repo1, TypeRepository repo2) throws Exception {
        return (args) -> {
//            ProductData p = repo1.findAll().iterator().next();
//            Job job = new Job();
//            repo1.delete(p);
//            ProductData p = new ProductData();
//            p.setName("p1");
////            repo2.save(job);
//            repo1.save(p);
//            ProductData p = repo1.findAllByName("p1").get(0);
//            p.setType(type);
//            repo2.save(type);
//            repo1.save(p);
//            System.out.println(repo1.findAllByName("p1").get(0).getID());
        };
    }

}
