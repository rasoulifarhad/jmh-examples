package com.farhad.example.jmh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 2)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
public class FileReaderBenchmarkDemo {
    
    @Param({"1000000"})
    private int fileSize;
    private File file;
    private FileInputStream fis;


    @Setup(Level.Trial)
    public void setup() throws IOException {
        System.out.println("Setup!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        file = File.createTempFile("FileReadBench", ".bin");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (int i = 0; i < fileSize; i++) {
                fos.write((byte) i);
            }
        } 
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        System.out.println("TearDown!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        file.delete();
    }

    @Setup(Level.Iteration)
    public void beforeIteration() throws FileNotFoundException {
        System.out.println("beforeIteration!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        fis = new FileInputStream(file);
    }

    @TearDown(Level.Iteration)
    public void afterIteration() throws IOException {
        System.out.println("afterIteration!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        fis.close();
    }

    @Benchmark
    public void benchmark() throws IOException {;
        int ret = fis.read();
        if ( ret == -1 ) {
            fis.close();
            fis = new FileInputStream(file);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                .include(FileReaderBenchmarkDemo.class.getSimpleName())
                                .build();
        new Runner(options).run();
    }
}
