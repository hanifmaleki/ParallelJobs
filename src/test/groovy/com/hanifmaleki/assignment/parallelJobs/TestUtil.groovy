package com.hanifmaleki.assignment.parallelJobs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TestUtil {

    final Logger logger = LoggerFactory.getLogger(TestUtil.class)

    public static final int RANDOM_UPPER_BOUND = 20000

    static IJob getANormalJob(String name) {
        Long randomSleepTime = new Random().nextInt(RANDOM_UPPER_BOUND)
        return getANormalJob(name, randomSleepTime)
    }

    static IJob getANormalJob(String name, Long randomSleepTime) {
        return { ->
            Thread.sleep(randomSleepTime)
            logger.info "Thread ${name} is executed successfully in ${randomSleepTime} milliseconds"
        }
    }

    static IJob getAnExceptionalJob(String name) {
        Long randomSleepTime = new Random().nextInt(RANDOM_UPPER_BOUND)
        return getAnExceptionalJob(name, randomSleepTime)
    }

    static IJob getAnExceptionalJob(String name, Long randomSleepTime) {
        return { ->
            Thread.sleep(randomSleepTime)
            logger.info "Thread ${name} is executed exceptionally in ${randomSleepTime} milliseconds"
            throw new RuntimeException("I am a runtime-exception")
        }
    }
    
}
