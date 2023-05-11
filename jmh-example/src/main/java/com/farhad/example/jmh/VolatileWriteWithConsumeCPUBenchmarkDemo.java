package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2)
// @Measurement(iterations = 20)
public class VolatileWriteWithConsumeCPUBenchmarkDemo {
    @Param({"0", "1", "2", "3", "4", "5"})
    private int tokens ;

    private int plainValue ;
    private volatile int volatileValue ;

    @Benchmark
    public void baseline_Plain() {
        Blackhole.consumeCPU(tokens);
    }

    @Benchmark
    public int baseline_Return_Constant() {
        Blackhole.consumeCPU(tokens);
        return 10;
    }

    @Benchmark
    public int inc_Plain_Value() {
        Blackhole.consumeCPU(tokens);
        return plainValue++;
    }

    @Benchmark
    public int inc_Volatile_Value() {
        Blackhole.consumeCPU(tokens);
        return volatileValue++;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                    .include(VolatileWriteWithConsumeCPUBenchmarkDemo.class.getSimpleName())
                                    .forks(1)
                                    .build();
        new Runner(options).run();
    }
}
