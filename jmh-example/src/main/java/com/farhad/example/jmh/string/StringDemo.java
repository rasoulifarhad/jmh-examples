package com.farhad.example.jmh.string;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import lombok.extern.slf4j.Slf4j;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Slf4j
public class StringDemo {
    private static final int LOOP_COUNT = 10_000_000;
    public void wideScopeStringConcat() {
        String wideScope ;
        for (int i = 0; i < LOOP_COUNT; i++) {
            wideScope = "" + i ;
        }
    }

    public void narrowScopeStringConcat() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            String wideScope = "" + i ;
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void doNoting() {

    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void objectCreation() {
        new Object() ;
    }

}
