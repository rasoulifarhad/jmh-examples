## JMH
# 
## JMH Benchmark Modes
# 
# JMH can run your benchmarks in different modes. 
# 
# The benchmark mode tells JMH what you want to measure. 
# 
# JMH offer these benchmark modes:
# 
#   - Throughput
#     Measures the number of operations per second, meaning the number of times per second 
#     your benchmark method could be executed. 
#   
#   - Average Time	
#     Measures the average time it takes for the benchmark method to execute (a single 
#     execution).
# 
#   - Sample Time	
#     Measures how long time it takes for the benchmark method to execute, including max, 
#     min time etc.
# 
#   - Single Shot Time
#     Measures how long time a single benchmark method execution takes to run. This is good 
#     to test how it performs under a cold start (no JVM warm up). 
# 
#   - All
#     Measures all of the above.
# 
# The default benchmark mode is Throughput.
# 
# Benchmark Time Units
# 
# MH enables you to specify what time units you want the benchmark results printed in.
# 
# The time unit will be used for all benchmark modes your benchmark is executed in.
# 
## Benchmark State
# 
# Sometimes you way want to initialize some variables that your benchmark code needs, 
# but which you do not want to be part of the code your benchmark measures. 
# 
# Such variables are called "state" variables. 
# 
# State variables are declared in special state classes, and an instance of that state 
# class can then be provided as parameter to the benchmark method. 
# 
## State Scope
# 
# A state object can be reused across multiple calls to your benchmark method. 
# 
#  JMH provides different "scopes" that the state object can be reused in. 
# 
# here state scope is specified in the parameter of the @State annotation.
# 
# The Scope class contains the following scope constants:
# 
#   - Thread
#     Each thread running the benchmark will create its own instance of the state object.
#  
#   - Group
#     Each thread group running the benchmark will create its own instance of the state object.
# 
#   - Benchmark
#     All threads running the benchmark share the same state object.
# 
# State Class Requirements
# 
# A JMH state class must obey the following rules:
# 
#   - The class must be declared public
# 
#   - If the class is a nested class, it must be declared static (e.g. public static class ...)
# 
#   - The class must have a public no-arg constructor (no parameters to the constructor).
# 
# When these rules are obeyed you can annotate the class with the @State annotation to make JMH 
# recognize it as a state class.
# 
# State Object @Setup and @TearDown
# 
# You can annotate methods in your state class with the @Setup and @TearDown annotations. 
# 
# The @Setup annotation tell JMH that this method should be called to setup the state object before 
# it is passed to the benchmark method.
# 
# The @TearDown annotation tells JMH that this method should be called to clean up ("tear down") the 
# state object after the benchmark has been executed.
# 
# The setup and tear down execution time is not included in the benchmark runtime measurements.
# 
# Notice also that the annotations take a parameter. There are three different values this parameter 
# can take. The value you set instruct JMH about when the method should be called. The possible 
# values are:
# 
#   - Level.Trial
#     The method is called once for each time for each full run of the benchmark. A full run means a 
#     full "fork" including all warmup and benchmark iterations.
# 
#   - Level.Iteration
#     The method is called once for each iteration of the benchmark.
# 
#   - Level.Invocation	
#     The method is called once for each call to the benchmark method.
# 
## Avoiding Dead Code Elimination
# 
# To avoid dead code elimination you must make sure that the code you want to measure does not look like 
# dead code to the JVM. There are two ways to do that.
# 
#   - Return the result of your code from the benchmark method.
# 
#   - Pass the calculated value into a "black hole" provided by JMH.
# 
## Constant Folding
# 
# Constant folding is another common JVM optimization. 
# 
# A calculation which is based on constants will often result in the exact same result, regardless of how many 
# times the calculation is performed. The JVM may detect that, and replace the calculation with the result of 
# the calculation. 
# 
# As an example, look at this benchmark:
# 
# public class MyBenchmark {
# 
#     @Benchmark
#     public int testMethod() {
#         int a = 1;
#         int b = 2;
#         int sum = a + b;
# 
#         return sum;
#     }
# }
# 
# The JVM may detect that the value of sum is based on the two constant values 1 and 2 in a and b. It may 
# thus replace the above code with this:
# 
# public class MyBenchmark {
# 
#     @Benchmark
#     public int testMethod() {
#         int sum = 3;
# 
#         return sum;
#     }
# }
# 
# Or even just return 3; directly.
# 
# The JVM could even continue and never call the testMethod() because it knows it always returns 3, and 
# just inline the constant 3 wherever the testMethod() was to be called.
# 
## Avoiding Constant Folding
# 
# To avoid constant folding you must not hardcode constants into your benchmark methods. Instead, the 
# input to your calculations should come from a state object. 
# 
# This makes it harder for the JVM to see that the calculations are based on constant values. 
# 
#   public class MyBenchmark {
#   
# Here is an example:
# 
#       @State(Scope.Thread)
#       public static class MyState {
#           public int a = 1;
#           public int b = 2;
#       }
#   
#       @Benchmark 
#       public int testMethod(MyState state) {
#           int sum = state.a + state.b;
#           return sum;
#       }
#   }
# 
# Terminology – Trial, Warmup and Iteration
# 
#  - Trial:
#    The JMH benchmark is run for a number of trials. 
#    Trials are also called as forks. 
# 
#  - Warmup:
#    For each fork, a number of iterations are configured as warmups.
#    This is to get the JVM to warmup the code we are measuring. 
#    This is important to avoid fluctuations or variations in the runtime once we start the actual iterations.
# 
#  - Iteration:
#    This is the actual benchmark code execution/iteration. 
#    The performance numbers from this will be output as the JMH benchmark result.
# 
# Each warmup iteration and measurement iteration is executed for a certain time. 
# 
# A JMH fork consists of a set of warmups (w) and a set of measurements (m). 
# 
## Annotations
# 
#   - BenchmarkMode
#   - OutputTimeUnit
#   - Benchmark
#      > Must be public
#      > The arguments may be one of the JMH’s State, Control or Blackhole classes.
#   - Fork
#      > This is used to tell the number of trials or forks.
#      > The value field is the number of forks. 
#      > It has a warmup field that configures the number of forks that must 
#        be treated as warmups.
#      > Example: @Fork(value = 5, warmups = 2)
#      > We here have 5 forks and among them 2 entire forks will be warmups.
# 
#   - Measurement
#      > It is used to set the default measurement parameters for the benchmark. 
#      > It allows to specify the number of iterations and the time for which 
#        each is to be executed. 
#      > Example: @Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
#      > We here specify 3 iterations each to be run for 1000 millisecond (1 second). 
#        The default time unit is seconds.
# 
#   - Warmup
#      > Warmup parameters are similar to that of Measurement except that it applies 
#        for the warmup runs.
#      > Example: @Warmup(iterations = 3, time = 2)
#      > This dedicates 3 full iterations as warmup each running for 2 seconds.
#  
# A State is a class-level annotation. We can pass any class annotated with @State as an argument to the 
# benchmark method.
# 
# Benchmark methods can reference the states, and JMH will inject the
# appropriate states while calling these methods. 
#
#
# Setting up the benchmarking project
# 
#  $ mvn archetype:generate \
#   -DinteractiveMode=false \
#   -DarchetypeGroupId=org.openjdk.jmh \
#   -DarchetypeArtifactId=jmh-java-benchmark-archetype \
#   -DgroupId=org.sample \
#   -DartifactId=test \
#   -Dversion=1.0
#
# See https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-1
# See https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-2
# See https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-3
#
# See https://github.com/openjdk/jmh-jdk-microbenchmarks
# See https://github.com/guozheng/jmh-tutorial
# See https://www.oracle.com/technical-resources/articles/java/architect-benchmarking.html
# See https://www.oracle.com/technetwork/articles/java/architect-benchmarking-2266277.html

