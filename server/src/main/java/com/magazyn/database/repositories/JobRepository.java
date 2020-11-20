package com.magazyn.database.repositories;

import com.magazyn.database.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<Job, Integer>{
}
