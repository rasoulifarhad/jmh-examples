package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * JMH Benchmark Modes
 * 
 * JMH can run your benchmarks in different modes. 
 * 
 * The benchmark mode tells JMH what you want to measure. 
 * 
 * JMH offer these benchmark modes:
 * 
 *   - Throughput
 *     Measures the number of operations per second, meaning the number of times per second 
 *     your benchmark method could be executed. 
 *   
 *   - Average Time	
 *     Measures the average time it takes for the benchmark method to execute (a single 
 *     execution).
 * 
 *   - Sample Time	
 *     Measures how long time it takes for the benchmark method to execute, including max, 
 *     min time etc.
 * 
 *   - Single Shot Time
 *     Measures how long time a single benchmark method execution takes to run. This is good 
 *     to test how it performs under a cold start (no JVM warm up). 
 * 
 *   - All
 *     Measures all of the above.
 * 
 * The default benchmark mode is Throughput.
 * 
 * Benchmark Time Units
 * 
 * MH enables you to specify what time units you want the benchmark results printed in.
 * 
 * The time unit will be used for all benchmark modes your benchmark is executed in.
 * 
 * Benchmark State
 * 
 * Sometimes you way want to initialize some variables that your benchmark code needs, 
 * but which you do not want to be part of the code your benchmark measures. 
 * 
 * Such variables are called "state" variables. 
 * 
 * State variables are declared in special state classes, and an instance of that state 
 * class can then be provided as parameter to the benchmark method. 
 * 
 * State Scope
 * 
 * A state object can be reused across multiple calls to your benchmark method. 
 * 
 *  JMH provides different "scopes" that the state object can be reused in. 
 * 
 * here state scope is specified in the parameter of the @State annotation.
 * 
 * The Scope class contains the following scope constants:
 * 
 *   - Thread
 *     Each thread running the benchmark will create its own instance of the state object.
 *  
 *   - Group
 *     Each thread group running the benchmark will create its own instance of the state object.
 * 
 *   - Benchmark
 *     All threads running the benchmark share the same state object.
 * 
 * State Class Requirements
 * 
 * A JMH state class must obey the following rules:
 * 
 *   - The class must be declared public
 * 
 *   - If the class is a nested class, it must be declared static (e.g. public static class ...)
 * 
 *   - The class must have a public no-arg constructor (no parameters to the constructor).
 * 
 * When these rules are obeyed you can annotate the class with the @State annotation to make JMH 
 * recognize it as a state class.
 * 
 * State Object @Setup and @TearDown
 * 
 * You can annotate methods in your state class with the @Setup and @TearDown annotations. 
 * 
 * The @Setup annotation tell JMH that this method should be called to setup the state object before 
 * it is passed to the benchmark method.
 * 
 * The @TearDown annotation tells JMH that this method should be called to clean up ("tear down") the 
 * state object after the benchmark has been executed.
 * 
 * The setup and tear down execution time is not included in the benchmark runtime measurements.
 * 
 * Notice also that the annotations take a parameter. There are three different values this parameter 
 * can take. The value you set instruct JMH about when the method should be called. The possible 
 * values are:
 * 
 *   - Level.Trial
 *     The method is called once for each time for each full run of the benchmark. A full run means a 
 *     full "fork" including all warmup and benchmark iterations.
 * 
 *   - Level.Iteration
 *     The method is called once for each iteration of the benchmark.
 * 
 *   - Level.Invocation	
 *     The method is called once for each call to the benchmark method.
 * 
 * Avoiding Dead Code Elimination
 * 
 * To avoid dead code elimination you must make sure that the code you want to measure does not look like 
 * dead code to the JVM. There are two ways to do that.
 * 
 *   - Return the result of your code from the benchmark method.
 * 
 *   - Pass the calculated value into a "black hole" provided by JMH.
 * 
 * Constant Folding
 * 
 * Constant folding is another common JVM optimization. 
 * 
 * A calculation which is based on constants will often result in the exact same result, regardless of how many 
 * times the calculation is performed. The JVM may detect that, and replace the calculation with the result of 
 * the calculation. 
 * 
 * As an example, look at this benchmark:
 * 
 * public class MyBenchmark {
 * 
 *     @Benchmark
 *     public int testMethod() {
 *         int a = 1;
 *         int b = 2;
 *         int sum = a + b;
 * 
 *         return sum;
 *     }
 * }
 * 
 * The JVM may detect that the value of sum is based on the two constant values 1 and 2 in a and b. It may 
 * thus replace the above code with this:
 * 
 * public class MyBenchmark {
 * 
 *     @Benchmark
 *     public int testMethod() {
 *         int sum = 3;
 * 
 *         return sum;
 *     }
 * }
 * 
 * Or even just return 3; directly.
 * 
 * The JVM could even continue and never call the testMethod() because it knows it always returns 3, and 
 * just inline the constant 3 wherever the testMethod() was to be called.
 * 
 * Avoiding Constant Folding
 * 
 * To avoid constant folding you must not hardcode constants into your benchmark methods. Instead, the 
 * input to your calculations should come from a state object. 
 * 
 * This makes it harder for the JVM to see that the calculations are based on constant values. 
 * 
 *   public class MyBenchmark {
 *   
 * Here is an example:
 * 
 *       @State(Scope.Thread)
 *       public static class MyState {
 *           public int a = 1;
 *           public int b = 2;
 *       }
 *   
 *       @Benchmark 
 *       public int testMethod(MyState state) {
 *           int sum = state.a + state.b;
 *           return sum;
 *       }
 *   }
 * 
 */
@Slf4j
public class MyBenchmark {
    
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkMethod( Blackhole blackhole ) {

        int x = 10 ;
        int y = 20;
        int z = x * y ;
        blackhole.consume(z);
    }

    @State(Scope.Thread)
    public static class MyState {

        public int x = 10;
        public int y = 20;
        public int z ;

        @Setup(Level.Trial)
        public void doSetup() {

            z = 0;
            log.info("Do Setup");

        }

        @TearDown(Level.Trial)
        public void doTearDown() {

            log.info("Do TearDown");
        } 
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkMethod2( Blackhole blackhole, MyState state ) {

        state.z = state.x * state.y ;

        blackhole.consume(state.z);

    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                                .include(MyBenchmark.class.getSimpleName())
                                .forks(1)
                                .build();

        new Runner(opt).run() ;
    }

}