package com.farhad.example.jmh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
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
public class FileChannelReadBenchmarkDemo {

    @Param({"1000000"})
    int fileSize;

    private File file;
    private FileChannel fc ;
    private ByteBuffer bb;

    @Setup(Level.Trial)
    public void setup() throws IOException{
        file = File.createTempFile("FileChannelReadBench", ".bin");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (int i = 0; i < fileSize; i++) {
                fos.write((byte)i);
            }
            bb = ByteBuffer.allocate(1);
        } 
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        file.delete();
    }

    @Setup(Level.Iteration)
    public void beforeIteration() throws IOException{
        fc = FileChannel.open(file.toPath(), StandardOpenOption.READ);
    }

    @TearDown(Level.Iteration)
    public void afterIteration() throws IOException {
        fc.close();
    }

    @Benchmark
    public void benchmark() throws IOException {
        int ret = fc.read(bb);
        bb.flip();
        if ( ret == -1 ) {
            fc.position(0);
        }
    }
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                                .include(FileReaderBenchmarkDemo.class.getSimpleName())
                                .build();
        new Runner(options).run();
    }
}
