package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkModes {
    

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(150);

    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAverageTime() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(150);

    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureSampleTime() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(150);

    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureSingleShotTime() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(150);

    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAll() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(150);

    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                                .include(BenchmarkMode.class.getSimpleName())
                                .forks(1)
                                .build();

        new Runner(opt).run();
    }

}
