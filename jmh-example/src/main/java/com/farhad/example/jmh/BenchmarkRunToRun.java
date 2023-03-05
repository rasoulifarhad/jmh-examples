package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkRunToRun {
    

    @State(Scope.Thread)
    public static class sleepyState {
        public long sleepTime;

        @Setup
        public void setup() {

            sleepTime = ( long ) Math.random() * 1000 ;
        }

    }

    @Benchmark
    @Fork(1)
    public void baseline(sleepyState state ) throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(state.sleepTime);
    }

    @Benchmark
    @Fork(5)
    public void benchmarkRun_1(sleepyState state ) throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(state.sleepTime);
    }

    @Benchmark
    @Fork(20)
    public void benchmarkRun_2(sleepyState state ) throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(state.sleepTime);
    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                            .include(BenchmarkRunToRun.class.getSimpleName())
                            .warmupIterations(0)
                            .measurementIterations(3)
                            .build();

        new Runner(opt).run();
    }

}
