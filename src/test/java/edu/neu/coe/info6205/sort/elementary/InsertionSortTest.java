/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.*;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.LazyLogger;
import edu.neu.coe.info6205.util.PrivateMethodTester;
import edu.neu.coe.info6205.util.StatPack;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ALL")
public class InsertionSortTest {
    public enum ArrayOrdering {
        RANDOM,
        ORDERED,
        PARTIALLY_ORDERED,
        REVERSE_ORDERED
    }
    @Test
    public void sort0() throws Exception {
        final List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Integer[] xs = list.toArray(new Integer[0]);
        final Config config = Config.setupConfig("true", "0", "1", "", "");
        Helper<Integer> helper = HelperFactory.create("InsertionSort", list.size(), config);
        helper.init(list.size());
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        final StatPack statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        SortWithHelper<Integer> sorter = new InsertionSort<Integer>(helper);
        sorter.preProcess(xs);
        Integer[] ys = sorter.sort(xs);
        assertTrue(helper.sorted(ys));
        sorter.postProcess(ys);
        final int compares = (int) statPack.getStatistics(InstrumentedHelper.COMPARES).mean();
        assertEquals(list.size() - 1, compares);
        final int inversions = (int) statPack.getStatistics(InstrumentedHelper.INVERSIONS).mean();
        assertEquals(0L, inversions);
        final int fixes = (int) statPack.getStatistics(InstrumentedHelper.FIXES).mean();
        assertEquals(inversions, fixes);
    }

    @Test
    public void sort1() throws Exception {
        final List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(2);
        list.add(1);
        Integer[] xs = list.toArray(new Integer[0]);
        BaseHelper<Integer> helper = new BaseHelper<>("InsertionSort", xs.length, Config.load(InsertionSortTest.class));
        GenericSort<Integer> sorter = new InsertionSort<Integer>(helper);
        Integer[] ys = sorter.sort(xs);
        assertTrue(helper.sorted(ys));
        System.out.println(sorter.toString());
    }

    @Test
    public void testMutatingInsertionSort() throws IOException {
        final List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(2);
        list.add(1);
        Integer[] xs = list.toArray(new Integer[0]);
        BaseHelper<Integer> helper = new BaseHelper<>("InsertionSort", xs.length, Config.load(InsertionSortTest.class));
        GenericSort<Integer> sorter = new InsertionSort<Integer>(helper);
        sorter.mutatingSort(xs);
        assertTrue(helper.sorted(xs));

    }

    @Test
    public void testStaticInsertionSort() throws IOException {
        final List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(2);
        list.add(1);
        Integer[] xs = list.toArray(new Integer[0]);
        InsertionSort.sort(xs);
        assertTrue(xs[0] < xs[1] && xs[1] < xs[2] && xs[2] < xs[3]);
    }

    @Test
    public void sort2() throws Exception {
        final Config config = Config.setupConfig("true", "0", "1", "", "");
        int n = 100;
        Helper<Integer> helper = HelperFactory.create("InsertionSort", n, config);
        helper.init(n);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        final StatPack statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        Integer[] xs = helper.random(Integer.class, r -> r.nextInt(1000));
        SortWithHelper<Integer> sorter = new InsertionSort<Integer>(helper);
        sorter.preProcess(xs);
        Integer[] ys = sorter.sort(xs);
        assertTrue(helper.sorted(ys));
        sorter.postProcess(ys);
        final int compares = (int) statPack.getStatistics(InstrumentedHelper.COMPARES).mean();
        // NOTE: these are suppoed to match within about 12%.
        // Since we set a specific seed, this should always succeed.
        // If we use true random seed and this test fails, just increase the delta a little.
        assertEquals(1.0, 4.0 * compares / n / (n - 1), 0.12);
        final int inversions = (int) statPack.getStatistics(InstrumentedHelper.INVERSIONS).mean();
        final int fixes = (int) statPack.getStatistics(InstrumentedHelper.FIXES).mean();
        System.out.println(statPack);
        assertEquals(inversions, fixes);
    }

    @Test
    public void sort3() throws Exception {
        final Config config = Config.setupConfig("true", "0", "1", "", "");
        int n = 100;
        Helper<Integer> helper = HelperFactory.create("InsertionSort", n, config);
        helper.init(n);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        final StatPack statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        Integer[] xs = new Integer[n];
        for (int i = 0; i < n; i++) xs[i] = n - i;
        SortWithHelper<Integer> sorter = new InsertionSort<Integer>(helper);
        sorter.preProcess(xs);
        Integer[] ys = sorter.sort(xs);
        assertTrue(helper.sorted(ys));
        sorter.postProcess(ys);
        final int compares = (int) statPack.getStatistics(InstrumentedHelper.COMPARES).mean();
        // NOTE: these are suppoed to match within about 12%.
        // Since we set a specific seed, this should always succeed.
        // If we use true random seed and this test fails, just increase the delta a little.
        assertEquals(4950, compares);
        final int inversions = (int) statPack.getStatistics(InstrumentedHelper.INVERSIONS).mean();
        final int fixes = (int) statPack.getStatistics(InstrumentedHelper.FIXES).mean();
        System.out.println(statPack);
        assertEquals(inversions, fixes);
    }

        @Test
        public void runBenchmarks() {
            int[] arraySizes = {100, 200, 400, 800, 1600};
            ArrayOrdering[] orderings =
                    {ArrayOrdering.RANDOM, ArrayOrdering.ORDERED, ArrayOrdering.PARTIALLY_ORDERED, ArrayOrdering.REVERSE_ORDERED};
            for(int size:arraySizes){
                for(ArrayOrdering ordering:orderings){
                    runBenchmark(size,ordering);
                }
            }

        }
        private void runBenchmark(int size, ArrayOrdering arrayOrdering) {
            System.out.format("Running Benchmark: Array Size: %d, Ordering: %s%n", size, arrayOrdering);


            Integer[] array = generateArray(size, arrayOrdering);

            // Warm-up
            for (int warmUp = 0; warmUp < 10; warmUp++) {
                InsertionSort.sort(Arrays.copyOf(array, array.length));
            }

            // Measure the actual sorting time
            long startTime = System.nanoTime();
            InsertionSort.sort(Arrays.copyOf(array, array.length));
            long endTime = System.nanoTime();

            long elapsedTime = endTime - startTime;
System.out.println("Timetaken:"+ elapsedTime+"nanoseconds");
        }
    private Integer[] generateArray(int size, ArrayOrdering arrayOrdering) {
        Integer[] array = new Integer[size];
            switch(arrayOrdering){
            case RANDOM:
                array=IntStream.range(0,size).boxed().toArray(Integer[]::new);
                shuffleArray(array);
                break;
            case ORDERED:
                array = IntStream.range(0, size).boxed().toArray(Integer[]::new);
                break;
            case PARTIALLY_ORDERED:
                array = IntStream.range(0, size).boxed().toArray(Integer[]::new);
                shuffleArrayPortion(array, size / 4); // Shuffle a portion of the array
                break;
            case REVERSE_ORDERED:
                array = IntStream.range(0, size).boxed().sorted(Comparator.reverseOrder()).toArray(Integer[]::new);
                break;
        }

        return array;
    }

    private void shuffleArray(Integer[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp=array[index];
            array[index]=array[i];
            array[i]=temp;
        }
    }

    private void shuffleArrayPortion(Integer[] array, int portionSize) {
        Random random = new Random();
        for (int i = 0; i < portionSize; i++) {
            int index1 = random.nextInt(array.length);
            int index2 = random.nextInt(array.length);

            int temp = array[index2];
            array[index2] = array[index1];
            array[index1] = temp;
        }
    }
    final static LazyLogger logger = new LazyLogger(InsertionSort.class);

}