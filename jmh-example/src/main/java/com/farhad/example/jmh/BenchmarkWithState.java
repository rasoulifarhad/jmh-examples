package com.farhad.example.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkWithState {
    

    @State(Scope.Benchmark)
    public static class MultiplierBenchmarkState {

        volatile double x = 8.0;

    }

    @State(Scope.Thread)
    public static class MultiplierThreadkState {

        volatile double x = 4.0;

    }

    @Benchmark
    public void mutiplyUnShared( MultiplierThreadkState state) {

        // ThreadState is the Scope.Thread, 
        // each thread will have it's own copy of the state
        state.x = state.x * state.x;

    }

    @Benchmark
    public void mutiplyShared(MultiplierBenchmarkState state) {

        // BenchmarkState is the Scope.Benchmark, 
        // all threads will share the state instance
        state.x = state.x * state.x;

    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                               .include(BenchmarkWithState.class.getSimpleName()) 
                               .threads(4)
                               .forks(1)
                               .build();

        new Runner(opt).run();
    }

}
