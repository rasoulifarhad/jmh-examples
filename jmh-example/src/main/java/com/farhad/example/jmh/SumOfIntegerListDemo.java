package com.farhad.example.jmh;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2)
@Warmup(iterations = 1)
public class SumOfIntegerListDemo {
    
    @State(Scope.Benchmark)
    public static class IntegerListSupplier {

        @Param({"100", "1000"})
        // @Param({"1000000", "10000000", "100000000"})
        public int listSize;

        private List<Integer> testIntList;
        
        @Setup(Level.Trial)
        public void setup() {
            System.out.println("Setup!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            testIntList = new Random()
                                .ints()
                                .limit(listSize)
                                .boxed()
                                .collect(Collectors.toList());
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            System.out.println("TearDown!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        public List<Integer> get() {
            return testIntList;
        }
    }

    public static class VolatileLong {
        private volatile long value = 0L;
        public long getValue() {
            return value ;
        }
        public synchronized void add(long amount) {
            this.value += amount;
        }
    }

    @Benchmark
    public void atomicLong(Blackhole blackhole, IntegerListSupplier state) {
        AtomicLong sum = new AtomicLong(0);
        state.get().parallelStream().forEach(sum::addAndGet);
        blackhole.consume(sum.get());
    }

    @Benchmark
    public void longAdder(Blackhole blackhole, IntegerListSupplier state) {
        LongAdder sum = new LongAdder();
        state.get().parallelStream().forEach(sum::add);
        blackhole.consume(sum.sum());
    }

    @Benchmark
    public void volatileLong(Blackhole blackhole, IntegerListSupplier state) {
        VolatileLong sum = new VolatileLong();
        state.get().parallelStream().forEach(sum::add);
        blackhole.consume(sum.getValue());
    }

    @Benchmark
    public void longStreamSum(Blackhole blackhole, IntegerListSupplier state) {
        long sum = state.get().parallelStream().mapToLong(v -> v).sum();
        blackhole.consume(sum);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                        .include(SumOfIntegerListDemo.class.getSimpleName())
                                        .result(String.format("target/%s_results_%s.csv", SumOfIntegerListDemo.class.getSimpleName(), LocalDateTime.now()))
                                        .resultFormat(ResultFormatType.CSV)
                                        .build();
        new Runner(options).run();
    }
}
