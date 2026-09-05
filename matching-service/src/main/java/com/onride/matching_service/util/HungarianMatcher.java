package com.onride.matching_service.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HungarianMatcher {

    private static final long INFINITY = Long.MAX_VALUE / 2;

    public MatchResult match(int riderCount, int driverCount, long[][] cost) {
        int size = Math.max(riderCount, driverCount);
        if (size == 0) {
            return new MatchResult(List.of(), List.of());
        }

        long[][] padded = pad(cost, riderCount, driverCount, size);
        int[] driverToRider = solve(padded, size);

        List<Match> matches = new ArrayList<>();
        boolean[] riderMatched = new boolean[riderCount];

        for (int driver = 1; driver <= size; driver++) {
            int rider = driverToRider[driver];
            if (rider <= riderCount && driver <= driverCount) {
                matches.add(new Match(rider - 1, driver - 1));
                riderMatched[rider - 1] = true;
            }
        }

        List<Integer> unmatchedRiderIndexes = new ArrayList<>();
        for (int i = 0; i < riderCount; i++) {
            if (!riderMatched[i]) {
                unmatchedRiderIndexes.add(i);
            }
        }

        return new MatchResult(matches, unmatchedRiderIndexes);
    }

    private long[][] pad(long[][] cost, int riderCount, int driverCount, int size) {
        long[][] padded = new long[size + 1][size + 1];
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                padded[i][j] = (i <= riderCount && j <= driverCount) ? cost[i - 1][j - 1] : 0;
            }
        }
        return padded;
    }

    private int[] solve(long[][] cost, int size) {
        long[] riderPotential = new long[size + 1];
        long[] driverPotential = new long[size + 1];
        int[] driverToRider = new int[size + 1];
        int[] previousColumn = new int[size + 1];

        for (int rider = 1; rider <= size; rider++) {
            driverToRider[0] = rider;
            int currentDriver = 0;
            long[] minReducedCost = new long[size + 1];
            Arrays.fill(minReducedCost, INFINITY);
            boolean[] visited = new boolean[size + 1];

            do {
                visited[currentDriver] = true;
                int assignedRider = driverToRider[currentDriver];
                long delta = INFINITY;
                int nextDriver = -1;

                for (int driver = 1; driver <= size; driver++) {
                    if (visited[driver]) {
                        continue;
                    }
                    long reducedCost = cost[assignedRider][driver] - riderPotential[assignedRider] - driverPotential[driver];
                    if (reducedCost < minReducedCost[driver]) {
                        minReducedCost[driver] = reducedCost;
                        previousColumn[driver] = currentDriver;
                    }
                    if (minReducedCost[driver] < delta) {
                        delta = minReducedCost[driver];
                        nextDriver = driver;
                    }
                }

                for (int driver = 0; driver <= size; driver++) {
                    if (visited[driver]) {
                        riderPotential[driverToRider[driver]] += delta;
                        driverPotential[driver] -= delta;
                    } else {
                        minReducedCost[driver] -= delta;
                    }
                }

                currentDriver = nextDriver;
            } while (driverToRider[currentDriver] != 0);

            while (currentDriver != 0) {
                int previous = previousColumn[currentDriver];
                driverToRider[currentDriver] = driverToRider[previous];
                currentDriver = previous;
            }
        }

        return driverToRider;
    }

    public record Match(int riderIndex, int driverIndex) {
    }

    public record MatchResult(List<Match> matches, List<Integer> unmatchedRiderIndexes) {
    }
}