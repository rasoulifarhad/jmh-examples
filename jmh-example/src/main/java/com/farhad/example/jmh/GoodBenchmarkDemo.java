package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
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
import org.openjdk.jmh.runner.options.TimeValue;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 3, jvmArgsAppend = {"-server"})
public class GoodBenchmarkDemo {
    
    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public static double constant(double x1, double y1, double x2, double y2) {
        return 0.0d;
    }

    @State(Scope.Thread)
    public static class Data {
        double x1 = 0.0d;
        double y1 = 0.0d;
        double x2 = 10.0d;
        double y2 = 10.0d;
    }

    @Benchmark
    public void baseline_return_void(){

    }

    @Benchmark
    public double baseline_return_zero() {
        return 0.0d;
    }

    @Benchmark
    public double constant(Data data) {
        return constant(data.x1, data.y1, data.x2, data.y2);
    }

    @Benchmark
    @Warmup(iterations = 20)
    @Measurement(iterations = 5, time = 3000, timeUnit = TimeUnit.MILLISECONDS)
    public double distance(Data data) {
        return distance(data.x1, data.y1, data.x2, data.y2);
    }
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                        .include(GoodBenchmarkDemo.class.getSimpleName())
                                        .warmupIterations(20)
                                        .measurementIterations(5)
                                        .measurementTime(TimeValue.milliseconds(3000))
                                        .jvmArgsAppend("-server")
                                        .forks(3)
                                        .build();
        new Runner(options).run();
    }
}
