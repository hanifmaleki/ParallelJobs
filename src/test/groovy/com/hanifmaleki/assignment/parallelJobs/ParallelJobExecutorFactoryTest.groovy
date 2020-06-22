package com.hanifmaleki.assignment.parallelJobs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ParallelJobExecutorFactoryTest extends Specification {

    @Autowired
    ParallelJobExecutorFactory factory

    def "threadCount is correctly set from config file"() {
        when:
            def count = factory.getThreadCount()
        then:
            count == 15
    }
}
