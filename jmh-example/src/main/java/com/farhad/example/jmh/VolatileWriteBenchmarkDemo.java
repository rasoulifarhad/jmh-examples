package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
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
@State(Scope.Thread)
@Warmup(iterations = 3)
@Measurement(iterations = 20)
public class VolatileWriteBenchmarkDemo {
    private int plainValue ;
    private volatile int volatileValue ;

    @Benchmark
    public int baseline() {
        return 40 ;
    }

    @Benchmark
    public int incPlainValue(){
        return plainValue++ ;
    }

    @Benchmark
    public int incVolatileValue() {
        return volatileValue++;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                        .include(VolatileWriteBenchmarkDemo.class.getSimpleName())
                                        .forks(1)
                                        .build();
        new Runner(options).run();
    }
}
