package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
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
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class BenchmarkSquare {
    
    @Param({"1","2","3","4","5"})
    int number;


    @Benchmark
    public void benchmarkSquare(Blackhole bh)  {

        int squared = number * number ;
        bh.consume(squared);

    }

    @Benchmark
    public int benchmarkSquareWithoutBlackhole()  {

        return  number * number ;

    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                                .include(BenchmarkSquare.class.getSimpleName())
                                // .param("10","20")
                                .build();

        new Runner(opt).run();
                            
    }
    
}
