package com.farhad.example.jmh;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WrongBenchmarkLoops {
    
    private static int N = 10_000_000;

    private static List<String> dataForTesting = createData();

    public void benchmarkLoopFor() {

        log.info("Benchmarking For loop....");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < dataForTesting.size(); i++) {
            String res = dataForTesting.get(i);
        }
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime ;

        log.info("For - Elapsed time in milliseconds; {}", diff);
    }

    public void benchmarkLoopWhile() {

        log.info("Benchmarking While loop....");
        long startTime = System.currentTimeMillis();
        int i = 0;
        while (i < dataForTesting.size()) {
            String res = dataForTesting.get(i);
            i++ ;
        }
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime ;

        log.info("While - Elapsed time in milliseconds; {}", diff);
    }

    private static List<String> createData() {

        List<String> data = new ArrayList<>() ;
        for (int i = 0; i < N; i++) {

            data.add(String.format("Data: %s", i));
            
        }

        return data;
    } 

    public static void main(String[] args) {
        
        WrongBenchmarkLoops wrongBenchmarkLoops = new WrongBenchmarkLoops();

        wrongBenchmarkLoops.benchmarkLoopFor();
        wrongBenchmarkLoops.benchmarkLoopWhile();
    }
}
