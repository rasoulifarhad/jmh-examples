package com.farhad.example.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1)
public class CollectionAndArraySumBenchmarkDemo {
    private static final int DATA_SIZE = 1_000_000;

    private List<Integer> intList = new ArrayList<>();
    private int[] intArray = new int[DATA_SIZE];

    @Setup
    public void setup_List_And_Array() {
        for (int i = 0; i < DATA_SIZE; i++) {
            intList.add(i);
            intArray[i] = i ;
        }
    }

    @Benchmark
    public int int_baseline_sum(Blackhole blackhole) {
        int sum = 0;
        for (int i = 0; i < DATA_SIZE; i++) {
            sum+= i;
        }
        blackhole.consume(sum);
        return sum;
    }

    @Benchmark
    public int int_list_sum(Blackhole blackhole) {
        int sum = 0;
        for (int i = 0; i < DATA_SIZE; i++) {
            sum+= intList.get(i);
        }
        blackhole.consume(sum);
        return sum;
    }

    @Benchmark
    public int int_array_sum(Blackhole blackhole) {
        int sum = 0;
        for (int i = 0; i < DATA_SIZE; i++) {
            sum+= intArray[i];
        }
        blackhole.consume(sum);
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                    .include(CollectionAndArraySumBenchmarkDemo.class.getSimpleName())
                                    .forks(1)
                                    .build();
        new Runner(options).run();
    }
}
