package com.magazyn.database.repositories;

import com.magazyn.database.Job;
import com.magazyn.database.JobId;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface JobRepository extends CrudRepository<Job, JobId>, AddNewJob{
    List<Job> findAllByDateBeforeAndDoneOrderByDate(Date to, boolean done);
    List<Job> findAllByDateBeforeAndDone(Date to, boolean done);
//    List<Job> findAllByDateBetweenAndDone(Date from, Date to, boolean done);
//    List<Job> findAllByDateAndDoneOrderByDate(Date date, boolean done);
    List<Job> findAllByDateBetweenAndDoneOrderByDate(Date from, Date to, boolean done);
    List<Job> findAllByAssignedAndDone(int id, boolean done);
    void deleteAllByAssignedAndDone(int id, boolean done);
}
