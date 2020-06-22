package com.hanifmaleki.assignment.parallelJobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ParallelJobExecutor implements IJob {
    final Logger logger = LoggerFactory.getLogger(ParallelJobExecutor.class);

    private final List<IJob> jobs;
    private final ExecutorService executor;
    private List<CompletableFuture<Void>> jobCompletaleFutures;
    private ParallelJobStatus status;

    protected ParallelJobExecutor(List<IJob> jobs, ExecutorService executor) {
        this.jobs = jobs;
        this.executor = executor;
        status = ParallelJobStatus.CREATED;
    }


    @Override
    public void execute() {
        checkIfStatusIsCorrect();
        createJobsExecutionStructure();
        try {
            executeJobListAndUpdateStatus();
        } catch (CompletionException exp) {
            handleException(exp);
        } finally {
            finalizeExecutor();
        }
    }

    private void finalizeExecutor() {
        logger.debug("Shutting down the executor");
        executor.shutdown();
    }

    private void handleException(CompletionException exp) {
        logger.debug("An exception raised during jobs execution. Cancelling reamining jobs");
        jobCompletaleFutures.forEach(computableFuture -> computableFuture.cancel(true));
        status = ParallelJobStatus.COMPLETED_WITH_EXCEPTION;
        throw new IJobException(exp.getMessage(), exp);
    }

    private void executeJobListAndUpdateStatus() {
        jobCompletaleFutures.forEach(CompletableFuture::join);
        logger.debug("Jobs have been executed successfully");
        status = ParallelJobStatus.COMPLETED;
    }

    private void createJobsExecutionStructure() {
        logger.debug("Start running list of jobs of size {}", jobs.size());
        status = ParallelJobStatus.RUNNING;
        jobCompletaleFutures = jobs.stream().map(this::getJobComputableFuture).collect(Collectors.toList());
    }

    private void checkIfStatusIsCorrect() {
        if(status!= ParallelJobStatus.CREATED){
            logger.error("Can not execute the list of jobs. The status is {}", status);
            throw new IJobException("Execute jobs in wrong status");
        }
    }

    private CompletableFuture<Void> getJobComputableFuture(IJob job) {
        Supplier<Void> supplier = () -> {
            job.execute();
            return null;
        };
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    public ParallelJobStatus getStatus() {
        return status;
    }
}