package com.magazyn.database;

import com.magazyn.JobType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    private JobType jobType;

//    @OneToOne
//    @JoinColumn(name = "product_id")
//    private Product allThings;

    private Integer assigned;

    private Date date = new Date();

    public Job(JobType jobType, Product product, Integer assigned) {
        this.jobType = jobType;
//        this.product = product;
        this.assigned = assigned;
    }

    public Job() {
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
