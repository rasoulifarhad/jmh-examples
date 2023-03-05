package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class BenchmarkFork {
    

    IntAdder twoIntAdder = new TwoIntAdder();
    IntAdder fourIntAdder = new FourIntAdder();

    public  interface IntAdder {
        
        int add(int delta);

    }

    public static class TwoIntAdder implements IntAdder {

        @Override
        public int add(int delta) {
            return delta + 2 ;
        }
        
    }

    public static class FourIntAdder implements IntAdder {

        @Override
        public int add(int delta) {
            return delta + 4 ;
        }
        
    }

    public int calcIntAdd(IntAdder adder) {

        int sum = 0 ;
        
        for (int i = 0; i < 10; i++) {

            sum += adder.add(i);
            
        }
        return sum ;
    }

    /**
     * Fork(0) helps to run in the same JVM.
     */
    @Benchmark
    @Fork(0)
    public int calcIntAdd_TwoIntAdder() {
        return calcIntAdd(twoIntAdder);
    }

    /**
     * Fork(0) helps to run in the same JVM.
     */
    @Benchmark
    @Fork(0)
    public int calcIntAdd_FourIntAdder() {
        return calcIntAdd(fourIntAdder);
    }

    /**
     * Fork(0) helps to run in the same JVM.
     */
    @Benchmark
    @Fork(0)
    public int calcIntAdd_TwoIntAdder_Again() {
        return calcIntAdd(fourIntAdder);
    }


    @Benchmark
    @Fork(1)
    public int calcIntAdd_TwoIntAdder_forked() {
        return calcIntAdd(twoIntAdder);
    }

    @Benchmark
    @Fork(1)
    public int calcIntAdd_FourIntAdder_Forked() {
        return calcIntAdd(fourIntAdder);
    }


    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                            .include(BenchmarkFork.class.getSimpleName())
                            .build();

        new Runner(opt).run();
    }
}
