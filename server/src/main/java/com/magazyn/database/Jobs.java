package com.magazyn.database;

import com.magazyn.JobType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    private JobType jobType;

//    @OneToOne
//    @JoinColumn(name = "product_id")
//    private AllThings allThings;

    private Integer assigned;

    private Date date = new Date();

    public Jobs(JobType jobType, AllThings allThings, Integer assigned) {
        this.jobType = jobType;
//        this.allThings = allThings;
        this.assigned = assigned;
    }

    public Jobs() {
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
