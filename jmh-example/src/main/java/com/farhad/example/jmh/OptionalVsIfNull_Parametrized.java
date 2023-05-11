package com.farhad.example.jmh;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class OptionalVsIfNull_Parametrized {
    
    @Param({"true", "false"})
    private boolean isNull;

    private String value ;
    private String answerWhenNull = "NULL";

    @Setup(Level.Trial)
    public void setup() {
        value = isNull ? null : "something"; 
    }

    @Benchmark
    public String baseline() {
        return value ;
    }

    @Benchmark
    public String ifNull() {
        String val = value ;
        if ( val != null ) {
            return val ;
        }
        return answerWhenNull ;
    }

    @Benchmark
    public String optionalWithExplicitGet() {
        Optional<String> optional = Optional.ofNullable(value);
        if ( optional.isPresent() ) {
            return optional.get() ;
        }
        return answerWhenNull ;
    }

    @Benchmark
    public String optionalWithOrElse() {
        return Optional.ofNullable(value)
                       .orElse(answerWhenNull);
    }

    @Benchmark
    public String optionalWithOrElseGet() {
        return Optional.ofNullable(value)
                        .orElseGet(() -> answerWhenNull);
    }

    public static void main(String[] args) throws RunnerException {
        Options op = new OptionsBuilder()
                                    .include(OptionalVsIfNull_Parametrized.class.getSimpleName())
                                    .forks(1)
                                    .build();
        new Runner(op).run();
    }

}
