package com.assessment.seerbit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmSolutions {
    public static void main(String[] args) {
        // Finding sum pair
        int[] arr = {1, 2, 3, 4, 5};
        int target = 7;
        boolean hasSumPair = hasSumPair(arr, target);
        System.out.println("Has sum pair: " + hasSumPair);

        // Finding low and high index
        int[] sortedArr = {1, 2, 2, 2, 3, 4, 5, 5, 6};
        int key = 2;
        int[] indexRange = findIndexRange(sortedArr, key);
        System.out.println("Low Index: " + indexRange[0]);
        System.out.println("High Index: " + indexRange[1]);

        // Merging overlapping intervals
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] mergedIntervals = mergeIntervals(intervals);

        // Print the merged intervals
        for (int[] interval : mergedIntervals) {
            System.out.println(interval[0] + ", " + interval[1]);
        }
    }

    public static boolean hasSumPair(int[] arr, int target) {
        Set<Integer> numSet = new HashSet<>();
        for (int num : arr) {
            int diff = target - num;
            if (numSet.contains(diff)) {
                return true;
            }
            numSet.add(num);
        }
        return false;
    }

    public static int[] findIndexRange(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        int lowIndex = -1;
        int highIndex = -1;

        // Find low index
        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == key) {
                lowIndex = mid;
                high = mid - 1; // Continue searching towards the left for a lower index
            } else if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        low = 0;
        high = arr.length - 1;

        // Find high index
        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == key) {
                highIndex = mid;
                low = mid + 1; // Continue searching towards the right for a higher index
            } else if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return new int[]{lowIndex, highIndex};
    }

    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        List<int[]> merged = new ArrayList<>();
        int[] currentInterval = intervals[0];

        for (int i = 1; i < intervals.length; i++) {
            int[] interval = intervals[i];

            if (interval[0] <= currentInterval[1]) {
                // Overlapping intervals, update the end time
                currentInterval[1] = Math.max(currentInterval[1], interval[1]);
            } else {
                // Non-overlapping interval, add the current interval to the merged list
                merged.add(currentInterval);
                currentInterval = interval;
            }
        }

        // Add the last interval
        merged.add(currentInterval);

        // Convert the list to a 2D array and return
        return merged.toArray(new int[merged.size()][]);
    }
}
