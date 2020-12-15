package com.magazyn.database.repositories;

import javax.persistence.EntityManager;

import com.magazyn.database.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AddNewJobImpl implements AddNewJob {

    @Autowired
    private EntityManager entity_manager;

    @Override
    public boolean add(Job job) {
        try {
        entity_manager.persist(job);
        entity_manager.flush();
        } catch (Exception exception) {
            return false;
        }

        return true;
    }
    
}
