package com.hanifmaleki.assignment.parallelJobs;

import java.util.concurrent.CompletionException;

public class IJobException extends RuntimeException{

    public IJobException(String message, CompletionException exp) {
        super(message, exp);
    }

    public IJobException(String message) {
        super(message);
    }
}
