package com.farhad.example.jmh;

/**
 * 
 * The bench method executes a benchmark expressed as a java.lang.Runnable. The
 * other parameters include a descriptive
 * name (name), a benchmark run duration (runMillis), the inner loop upper bound
 * (loop), the number of warm-up rounds
 * (warmup), and the number of measured rounds (repeat).
 */
public class WrongBenchDemo {

    static final long RUN_MILLIS = 4000;
    static final int  REPEAT     = 10;
    static final int  WARMUP     = 15;
    static final int  LOOP       = 10_000;

    public static class Benchmark_With_Distance_Constant_Nothing_Order {

        /**
         * Result :
         * 
         * distance -> [ ~56846438 ops/ms ]
         * constant -> [ ~1016775 ops/ms  ]
         * nothing  -> [ ~493004 ops/ms   ] 
         * 
         * doing nothing is slower than returning a constant, which in turn is slower than square root operation that's quite expensive! 
         */
        public static void main(String[] args) {
            WrongBenchmark.bench("distance", 
                                    RUN_MILLIS, 
                                    LOOP, 
                                    WARMUP, 
                                    REPEAT,
                                    () -> Distance.distance(0.0d, 0.0d, 10.0D, 10.0D));

            WrongBenchmark.bench("constant", 
                                    RUN_MILLIS, 
                                    LOOP, 
                                    WARMUP, 
                                    REPEAT,
                                    () -> Distance.constant(0.0d, 0.0d, 10.0D, 10.0D));
                            
            WrongBenchmark.bench("nothing", 
                                    RUN_MILLIS, 
                                    LOOP, 
                                    WARMUP, 
                                    REPEAT,
                                    () -> Distance.nothing());
        }
    }
    public static class Benchmark_With_Nothing_Constant_Distance_Order {

        /**
         * Result :
         * 
         * distance -> [ ~491818 ops/ms   ]
         * constant -> [ ~1010800 ops/ms  ]
         * nothing  -> [ ~56799659 ops/ms ] 
         * 
         * this time, with a different benchmark ordering
         */
        public static void main(String[] args) {
            WrongBenchmark.bench("nothing", 
                                    RUN_MILLIS, 
                                    LOOP, 
                                    WARMUP, 
                                    REPEAT,
                                    () -> Distance.nothing());

            WrongBenchmark.bench("constant", 
                                    RUN_MILLIS, 
                                    LOOP, 
                                    WARMUP, 
                                    REPEAT,
                                    () -> Distance.constant(0.0d, 0.0d, 10.0D, 10.0D));

            WrongBenchmark.bench("distance", 
                                    RUN_MILLIS, 
                                    LOOP, 
                                    WARMUP, 
                                    REPEAT,
                                    () -> Distance.distance(0.0d, 0.0d, 10.0D, 10.0D));
        }
    }
    /**
     * By repeating the process for each benchmark alone, we get the following results:
     * 
     * distance -> [ ~56582461 ops/ms ]
     * constant -> [ ~56554361 ops/ms ]
     * nothing  -> [ ~56743410 ops/ms ]
     */
    public static class Benchmark_Nothing_Constant_Distance_Alone {
        /**
         * By repeating the process for each benchmark alone, we get the following results:
         * 
         * nothing  -> [ ~56743410 ops/ms ]
         */
        public static class Benchmark_Nothing {
            public static void main(String[] args) {
                WrongBenchmark.bench("nothing", 
                                            RUN_MILLIS, 
                                            LOOP, 
                                            WARMUP, 
                                            REPEAT,
                                            () -> Distance.nothing());
            }
        }
        /**
         * By repeating the process for each benchmark alone, we get the following results:
         * 
         * constant -> [ ~56554361 ops/ms ]
         */
        public static class Benchmark_Constant {
            public static void main(String[] args) {
                WrongBenchmark.bench("constant", 
                                            RUN_MILLIS, 
                                            LOOP, 
                                            WARMUP, 
                                            REPEAT,
                                            () -> Distance.constant(0.0d, 0.0d, 10.0D, 10.0D)); 
            }
        }
        /**
         * By repeating the process for each benchmark alone, we get the following results:
         * 
         * distance -> [ ~56582461 ops/ms ]
         */
        public static class Benchmark_Distance {
            public static void main(String[] args) {
                WrongBenchmark.bench("distance", 
                                            RUN_MILLIS, 
                                            LOOP, 
                                            WARMUP, 
                                            REPEAT,
                                            () -> Distance.distance(0.0d, 0.0d, 10.0D, 10.0D));
            }
        }
        /**
         * By returning result
         * 
         * distance -> [ ~39632956 ops/ms ]
         */
        public static class Benchmark_Distance_With_Return_Result {
            static double resultDistance = 0.0d;
            public static void main(String[] args) {
                WrongBenchmark.bench("distance", 
                                            RUN_MILLIS, 
                                            LOOP, 
                                            WARMUP, 
                                            REPEAT,
                                            () -> resultDistance =  Distance.distance(0.0d, 0.0d, 10.0D, 10.0D));
                System.out.println(resultDistance);
            }
        }
        /**
         * By returning result
         * 
         * constant -> [ ~40358289 ops/ms ]
         */
        public static class Benchmark_Constant_With_Return_Result {
            static double resultDistance = 0.0d;
            public static void main(String[] args) {
                WrongBenchmark.bench("distance", 
                                            RUN_MILLIS, 
                                            LOOP, 
                                            WARMUP, 
                                            REPEAT,
                                            () -> resultDistance =  Distance.constant(0.0d, 0.0d, 10.0D, 10.0D));
                System.out.println(resultDistance);
            }
        }

    }
    public static class WrongBenchmark {

        public static void bench(String name, long runMillis, int loop,
                int warmup, int repeat, Runnable runnable) {
            System.out.println("Running: " + name);
            int max = repeat + warmup;
            long average = 0L;
            for (int i = 0; i < max; i++) {
                long nops = 0;
                long duration = 0L;
                long start = System.currentTimeMillis();
                while (duration < runMillis) {
                    for (int j = 0; j < loop; j++) {
                        runnable.run();
                        nops++;
                    }
                    duration = System.currentTimeMillis() - start;
                }
                long throughput = nops / duration;
                boolean benchRun = i >= warmup;
                if (benchRun) {
                    average = average + throughput;
                }
                System.out.print(throughput + " ops/ms" + (!benchRun ? " (warmup) | " : " | "));
            }
            average = average / repeat;
            System.out.println("\n[ ~" + average + " ops/ms ]\n");
        }
    }

    public static class Distance {

        // Computes the Euclidean distance between two points (x1, y1) and (x2, y2).
        public static double distance(double x1, double y1, double x2, double y2) {
            double dx = x2 - x1;
            double dy = y2 - y1;
            return Math.sqrt((dx * dx) + (dy * dy));
        }

        public static double constant(double x1, double y1, double x2, double y2) {
            return 0.0d;
        }

        public static void nothing() {

        }
    }
}