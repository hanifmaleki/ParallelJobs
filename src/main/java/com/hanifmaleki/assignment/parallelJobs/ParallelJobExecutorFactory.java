package com.hanifmaleki.assignment.parallelJobs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@SpringBootConfiguration
@EnableAutoConfiguration
public class ParallelJobExecutorFactory {

    @Value("${thread.count:10}")
    private int threadCount;

    public ParallelJobExecutor createParallelJobExecutor(List<IJob> jobs){
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        return new ParallelJobExecutor(jobs, executor);
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

}
