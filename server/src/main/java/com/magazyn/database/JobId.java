package com.magazyn.database;

import com.magazyn.JobType;

import java.io.Serializable;

public class JobId implements Serializable {
    private Product product;

    private JobType jobType;

    public JobId(Product product, JobType jobType) {
        this.product = product;
        this.jobType = jobType;
    }

    //TODO hashCODE() equals()
}
