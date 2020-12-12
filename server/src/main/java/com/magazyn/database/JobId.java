package com.magazyn.database;

import com.magazyn.JobType;

import java.io.Serializable;

public class JobId implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7702272437903926803L;

    private Product product;

    private JobType jobType;

    public JobId(Product product, JobType jobType) {
        this.product = product;
        this.jobType = jobType;
    }

    @Override
    public boolean equals(Object other_obj) {
        if (!(other_obj instanceof JobId)) {
            return false;
        }

        JobId other = (JobId)other_obj;
        return other.jobType == jobType && other.product.equals(product);
    }

    @Override
    public int hashCode() {
        int hash = jobType.hashCode();
        hash ^= product.hashCode() + 0x9e3779b9 + (hash << 6) + (hash >> 2);

        return hash;
    }
}
