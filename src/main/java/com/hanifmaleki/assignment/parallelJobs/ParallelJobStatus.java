package com.hanifmaleki.assignment.parallelJobs;

public enum ParallelJobStatus {
    CREATED("created"), RUNNING("running"), COMPLETED("completed"), COMPLETED_WITH_EXCEPTION("completed with exception");

    private final String status;

    ParallelJobStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
