package com.magazyn.database.repositories;

import com.magazyn.database.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface JobRepository extends CrudRepository<Job, Integer>{
    List<Job> findAllByAssigned(int id);
    List<Job> findAllByDateBefore(Date from, Date to);
    void deleteAllByAssigned(int id);
}
