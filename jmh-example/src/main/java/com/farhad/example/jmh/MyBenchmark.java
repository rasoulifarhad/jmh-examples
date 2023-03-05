package com.farhad.example.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBenchmark {
    
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkMethod( Blackhole blackhole ) {

        int x = 10 ;
        int y = 20;
        int z = x * y ;
        blackhole.consume(z);
    }

    @State(Scope.Thread)
    public static class MyState {

        public int x = 10;
        public int y = 20;
        public int z ;

        @Setup(Level.Trial)
        public void doSetup() {

            z = 0;
            log.info("Do Setup");

        }

        @TearDown(Level.Trial)
        public void doTearDown() {

            log.info("Do TearDown");
        } 
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkMethod2( Blackhole blackhole, MyState state ) {

        state.z = state.x * state.y ;

        blackhole.consume(state.z);

    }

        /*
     * ============================== HOW TO RUN THIS TEST: ====================================
     *
     * Note the baseline is random within [0..1000] msec; and both forked runs
     * are estimating the average 500 msec with some confidence.
     *
     * You can run this test:
     *
     * a) Via the command line:
     *    $ mvn clean install
     *    $ java -jar target/benchmarks.jar JMHSample_13 -wi 0 -i 3
     *    (we requested no warmup, 3 measurement iterations; there are also other options, see -h)
     *
     * b) Via the Java API:
     *    (see the JMH homepage for possible caveats when running from IDE:
     *      http://openjdk.java.net/projects/code-tools/jmh/)
     */

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                                .include(MyBenchmark.class.getSimpleName())
                                .forks(1)
                                .build();

        new Runner(opt).run() ;
    }

}
