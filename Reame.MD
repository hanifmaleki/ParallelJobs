#Parallel Jobs
In this programming assignment, a java library is developed that is capable of running multiple jobs in parallel.
##Files

```
public interface IJob {
    public void execute();
}
```


##Tasks
* In our backend application, there are multiple classes implementing the IJob interface.
* Each of these implementations performs certain operations and is scheduled to run at a certain time on our servers.
* Because the amount of jobs we have to run increases steadily, we want to create a new job, which takes a list of IJob instances and runs them in parallel.
* The amount of threads that the new implementation is using to run the jobs in parallel should be configurable.
* If one of the jobs throws an exception:
    * All other currently running jobs should be stopped as well, and
    * The `execute()' method should pass the exception to the caller.
* To make sure the implementation works as designed UnitTests are required.

## Deliverables
* New IJob implementation including UnitTests
