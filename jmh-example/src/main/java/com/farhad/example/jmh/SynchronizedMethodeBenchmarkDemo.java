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
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 20)
public class SynchronizedMethodeBenchmarkDemo {
    
    public static class SynchoronizetMethod {
        

        private int counter = 0;
        private int intrinsicLockCounter = 0 ;
        public int increaseCounter() {
            return counter++;
        }
        public int increaseCounterUsingIntrinsicLock() {
            return intrinsicLockCounter++;
        }
    }

    private SynchoronizetMethod synchoronizetMethod = new SynchoronizetMethod();
    @Benchmark
    public void increaseCounter(Blackhole blackhole) {
        int counter =  synchoronizetMethod.increaseCounter();
        blackhole.consume(counter);
    }
    @Benchmark
    public void increaseCounterUsingIntrinsicLock(Blackhole blackhole) {
        int counter  = synchoronizetMethod.increaseCounterUsingIntrinsicLock();
        blackhole.consume(counter);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                        .include(SynchronizedMethodeBenchmarkDemo.class.getSimpleName())
                                        .forks(1)
                                        .build();
        new Runner(options).run();
    }
}
