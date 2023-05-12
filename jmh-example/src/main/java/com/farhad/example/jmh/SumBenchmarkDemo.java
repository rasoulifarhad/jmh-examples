package com.farhad.example.jmh;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 3,
    jvmArgsAppend = {"-server", "-disablesystemassertions"})
public class SumBenchmarkDemo {
    
    @State(Scope.Thread)
    public static class AdditionState {
        int x ;
        int y ;

        @Setup(Level.Iteration)
        public void setupAdditionParameters() {
            Random random = new Random();
            x = random.nextInt();
            y = random.nextInt();
        }

        @TearDown(Level.Iteration)
        public void tearDownAdditionParameters() {
            x = y = 0;
        }
    }

    @Benchmark
    @Warmup(iterations = 10, time = 3, timeUnit = TimeUnit.SECONDS )
    public int baseline(AdditionState state) {
        return state.x;
    }

    @Benchmark
    @Warmup(iterations = 10, time = 3, timeUnit = TimeUnit.SECONDS )
    public int sum(AdditionState state) {
        return state.x + state.y;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                        .include(SumBenchmarkDemo.class.getSimpleName())
                                        // .forks(3)
                                        // .jvmArgsAppend("-server", "-disablesystemassertions")
                                        // .warmupIterations(10)
                                        // .warmupTime(TimeValue.milliseconds(3000))
                                        // .measurementIterations(5)
                                        // .measurementTime(TimeValue.milliseconds(3000))
                                        .build();
        new Runner(options).run();
    }
}
