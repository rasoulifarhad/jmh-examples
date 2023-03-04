package com.farhad.example.jmh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class BenchmarkLoops {
    
    @Param({"10000000"})
    private int N;

    private List<String> dataForTestong ;

    @Setup
    public void Setup() {

        dataForTestong = createData();
    }

    @Benchmark
    public void benchmarkLoopFor(Blackhole blackhole) {

        for (int i = 0; i < dataForTestong.size(); i++) {
            String res = dataForTestong.get(i);
            blackhole.consume(res);
        }
    }

    @Benchmark
    public void benchmarkLoopWhile( Blackhole blackhole ) {

        int i =0;
        while ( i < dataForTestong.size() ) {

            String res = dataForTestong.get(i);
            blackhole.consume(res);
            i++ ;
        }
    }

    @Benchmark
    public void benchmarkLoopForEach( Blackhole blackhole ) {

        for (String res : dataForTestong) {
                blackhole.consume(res);
        }

    }

    @Benchmark
    public void benchmarkLoopIterator( Blackhole blackhole ) {

        Iterator<String> it = dataForTestong.iterator();

        while (it.hasNext()) {
            
            String res = it.next() ;
            blackhole.consume(res);
        }
    }

    private List<String> createData() {

        List<String> data = new ArrayList<>() ;
        for (int i = 0; i < N; i++) {

            data.add(String.format("Data: %s", i));
            
        }
        return data;
    }

    public static void main(String[] args) throws IOException, RunnerException {

        Options opt = new OptionsBuilder()
                                .include(BenchmarkLoops.class.getSimpleName())
                                .forks(1)
                                .build();

        new Runner(opt).run();
        // org.openjdk.jmh.Main.main(args);
    }

}
