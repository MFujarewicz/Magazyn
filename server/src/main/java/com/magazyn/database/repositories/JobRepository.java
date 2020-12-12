package com.magazyn.database.repositories;

import com.magazyn.database.Job;
import com.magazyn.database.JobId;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface JobRepository extends CrudRepository<Job, JobId>, AddNewJob{
    List<Job> findAllByAssigned(int id);
    List<Job> findAllByDateBeforeOrderByDate(Date to);
    List<Job> findAllByAssignedAndDone(int id, boolean done);
    void deleteAllByAssigned(int id);
}
