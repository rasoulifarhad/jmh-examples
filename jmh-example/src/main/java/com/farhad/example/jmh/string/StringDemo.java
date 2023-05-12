package com.farhad.example.jmh.string;

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
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
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

    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @State(Scope.Benchmark)
    @Warmup(iterations = 1)
    @Measurement(iterations = 2)
    public static class String_StringBuffer_StringBuilder_benchmark_Demo {

        private static final int LOOP_COUNT2 = 100_000 ;
        
        @Benchmark
        public String string_append_a_in_loop(Blackhole blackhole) {
            String s = new String() ;
            for (int i = 0; i < LOOP_COUNT2; i++) {
                s += "a"; 
            }
            blackhole.consume(s);
            return s;
        }

        @Benchmark
        public String stringBuffer_append_a_in_loop(Blackhole blackhole) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < LOOP_COUNT2; i++) {
                sb.append("a");
            }
            blackhole.consume(sb);
            return sb.toString();
        }

        @Benchmark
        public String stringBuilder_append_a_in_loop(Blackhole blackhole) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < LOOP_COUNT2; i++) {
                sb.append("a");
            }
            blackhole.consume(sb);
            return sb.toString();
        }

        public static void main(String[] args) throws RunnerException {
            
            Options options = new OptionsBuilder()
                                            .include(String_StringBuffer_StringBuilder_benchmark_Demo.class.getSimpleName())
                                            .forks(1)
                                            .build();
            new Runner(options).run();
        }

    }

}
