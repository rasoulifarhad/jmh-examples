package com.farhad.example.jmh;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ConcurrentBenchmarkDemo {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                        .include(ConcurrentBenchmarkDemo.class.getSimpleName())
                                        .warmupIterations(3)
                                        .warmupTime(TimeValue.milliseconds(2000))
                                        .measurementIterations(5)
                                        .measurementTime(TimeValue.milliseconds(5000))
                                        .forks(3)
                                        .result(String.format("target/%s_results_%s.csv", SumOfIntegerListDemo.class.getSimpleName(), LocalDateTime.now()))
                                        .resultFormat(ResultFormatType.CSV)
                                        .build();
        new Runner(options).run();
    }

    @State(Scope.Group)
    @Threads(8)
    public static class Pessimistic {
        long counter = 0L;
        final Object lock = new Object();

        @Setup
        public void setup(){
            counter = 0L;
        }
        public long get() {
            synchronized(lock) {
                return counter ;
            }
        }

        public long incrementAndGet() {
            synchronized(lock) {
                return ++counter;
            }
        }
    }

    @Benchmark
    @Group("pessimistic_more_readers")
    @GroupThreads(7)
    public long pessimistic_more_readers_get(Pessimistic state){
        return state.get();
    }

    @Benchmark
    @Group("pessimistic_more_readers")
    @GroupThreads(1)
    public long pessimistic_more_readers_incrementAndGet(Pessimistic state){
        return state.incrementAndGet();
    }

    @Benchmark
    @Group("pessimistic_more_writers")
    @GroupThreads(1)
    public long pessimistic_more_writers_get(Pessimistic state){
        return state.get();
    }

    @Benchmark
    @Group("pessimistic_more_writers")
    @GroupThreads(7)
    public long pessimistic_more_writers_incrementAndGet(Pessimistic state){
        return state.incrementAndGet();
    }
    

    @State(Scope.Group)
    @Threads(8)
    public static class Optimistic {
        AtomicLong counter;

        @Setup
        public void setup(){
            counter = new AtomicLong(0);
        }
        public long get() {
            return counter.get() ;
        }

        public long incrementAndGet() {
            return counter.incrementAndGet();
        }
    }

    @Benchmark
    @Group("optimistic_more_readers")
    @GroupThreads(7)
    public long optimistic_more_readers_get(Optimistic state){
        return state.get();
    }

    @Benchmark
    @Group("optimistic_more_readers")
    @GroupThreads(1)
    public long optimistic_more_readers_incrementAndGet(Optimistic state){
        return state.incrementAndGet();
    }

    @Benchmark
    @Group("optimistic_more_writers")
    @GroupThreads(1)
    public long optimistic_more_writers_get(Optimistic state){
        return state.get();
    }

    @Benchmark
    @Group("optimistic_more_writers")
    @GroupThreads(7)
    public long optimistic_more_writers_incrementAndGet(Optimistic state){
        return state.incrementAndGet();
    }
    
}
