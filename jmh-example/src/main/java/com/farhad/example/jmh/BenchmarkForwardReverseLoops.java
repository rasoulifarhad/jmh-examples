package com.farhad.example.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
public class BenchmarkForwardReverseLoops {
    

    @Param({"1000000", "10000000"})
    private int N;

    private List<String> dataForTesting;

    @Setup
    public void setup() {
        dataForTesting  = createData();
    }

    @Benchmark
    public void benchmarkForwardLoopFor(Blackhole blackhole) {

        for (int i = 0; i < dataForTesting.size(); i++) {
            String res = dataForTesting.get(i);
            blackhole.consume(res);
        }
    }

    @Benchmark
    public void benchmarkReverseLoopFor( Blackhole blackhole ) {

        for (int i = dataForTesting.size() - 1 ; i >= 0 ; i--) {

            String res = dataForTesting.get(i);
            blackhole.consume(res);
        }
    }
    private List<String> createData() {

        List<String> data = new ArrayList<>();

        for (int i = 0; i < N; i++) {

            data.add(String.format("Data: %s", i));
            
        }
        return data;
    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                                .include(BenchmarkForwardReverseLoops.class.getSimpleName())
                                .forks(1)
                                .build();

        new Runner(opt).run();

    }
 }
