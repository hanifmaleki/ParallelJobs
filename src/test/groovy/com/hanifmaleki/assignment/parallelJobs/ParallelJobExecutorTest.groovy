package com.hanifmaleki.assignment.parallelJobs


import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import static ParallelJobStatus.COMPLETED
import static ParallelJobStatus.COMPLETED_WITH_EXCEPTION

class ParallelJobExecutorTest extends Specification {

    public static final int THREAD_COUNT = 10

    ExecutorService executorService

    def setup() {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT)
    }

    def "A set of normal jobs are executed successfully with ParallelJobExecutor"() {
        given:
            def jobList = (1..THREAD_COUNT).collect { TestUtil.getANormalJob("${it}", 1500 * it) }.toList()
            def parallelJobExecutor = new ParallelJobExecutor(jobList, executorService)
        when:
            def startTime = System.currentTimeMillis()
            parallelJobExecutor.execute()
            def endTime = System.currentTimeMillis()
        then:
            notThrown IJobException
            parallelJobExecutor.status == COMPLETED
            parallelJobExecutor.jobCompletaleFutures.each { it.isDone() }
            parallelJobExecutor.jobCompletaleFutures.each { !it.isCancelled() }
            endTime - startTime > 1500 * 10
    }

    def "When there is an exception in a task all of them should be cancelled"() {
        given:
            def jobList = (1..THREAD_COUNT - 1).collect { TestUtil.getANormalJob("${it}", 2000 * it) }.toList()
            def exceptionalTask = getAnExceptionalJob("exceptional", 11000)
            jobList.add(exceptionalTask)
            def parallelJobExecutor = new ParallelJobExecutor(jobList, executorService)
        when:
            parallelJobExecutor.execute()
        then:
            thrown IJobException
            parallelJobExecutor.status == COMPLETED_WITH_EXCEPTION
            parallelJobExecutor.jobCompletaleFutures[1..5].each {
                !it.isCancelled()
            }
            parallelJobExecutor.jobCompletaleFutures[6..9].each {
                it.isCancelled()
            }
    }

    def "When ParallelJobExecutor is executed in the status != CREATED a corresponding exception is thrown"() {
        given:
            def jobList = (1..THREAD_COUNT).collect { TestUtil.getANormalJob("${it}", 1500 * it) }.toList()
            def parallelJobExecutor = new ParallelJobExecutor(jobList, executorService)
        when:
            parallelJobExecutor.execute()
            parallelJobExecutor.execute()
        then:
            thrown IJobException
    }

    def "The ParallelJobExecutor is does not create more than predefined number of threads"() {
        given:
            def jobList = (1..4 * THREAD_COUNT).collect { TestUtil.getANormalJob("${it}", 1500) }.toList()
            def parallelJobExecutor = new ParallelJobExecutor(jobList, executorService)
        when:
            def startTime = System.currentTimeMillis()
            parallelJobExecutor.execute()
            def endTime = System.currentTimeMillis()
        then:
            notThrown IJobException
            parallelJobExecutor.status == COMPLETED
            parallelJobExecutor.jobCompletaleFutures.each { it.isDone() }
            parallelJobExecutor.jobCompletaleFutures.each { !it.isCancelled() }
            endTime - startTime > 1500 * 4
    }


}
