package com.farhad.example.jmh;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class CollectionFinderBenchmarkDemo {
    
    private static final int SET_SIZE = 10_000;
    private Set<String> hashSet;
    private Set<String> treeSet;

    private String stringToFind = "8765";

    @Setup
    public void setupCoillections() {
        hashSet = new HashSet<>(SET_SIZE);
        treeSet = new TreeSet<>();

        for (int i = 0; i < SET_SIZE; i++) {
            final String value = String.valueOf(i);
            hashSet.add(value);
            treeSet.add(value);
        }

        stringToFind = String.valueOf(new Random().nextInt(SET_SIZE));  
    }

    @Benchmark
    public void  benchmarkHashSet(Blackhole blackhole) {
        blackhole.consume( hashSet.contains(stringToFind));
    }

    @Benchmark
    public void  benchmarkTreeSet(Blackhole blackhole) {
        blackhole.consume( treeSet.contains(stringToFind));
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                      .include(CollectionFinderBenchmarkDemo.class.getSimpleName())  
                                      .forks(1)
                                      .build();
        new Runner(options).run(); 
    }

}
