package com.farhad.example.jmh.basic;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1)
// @Measurement(iterations = 8)
public class deadCodeDemo {
    
    @Benchmark
    public void doNoting() {

    }

    @Benchmark
    public void objectCreation() {
        new Object() ;
    }

    public static void main(String[] args) throws RunnerException {
        Options op = new OptionsBuilder()
                                    .include(deadCodeDemo.class.getSimpleName())
                                    .forks(1)
                                    .build();
        new Runner(op).run();
    }

    static class BenchmarkRunner {
        public static void main(String[] args) throws IOException {
            org.openjdk.jmh.Main.main(args);
        }
    }
}
