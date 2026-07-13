package lb.strategy;

import lb.core.BalancerState;

public class WeightedLeastConnectionsStrategy implements BalancerStrategy {

    private final BalancerState state;

    private static final double ALPHA = 0.3;

    private final double[] ewmaLatency;

    public WeightedLeastConnectionsStrategy(BalancerState state) {
        this.state = state;
        this.ewmaLatency = new double[state.servers.length];
    }

    @Override
    public int chooseServer() {

        int best = -1;
        double bestScore = Double.MAX_VALUE;

        for (int i = 0; i < state.servers.length; i++) {

            if (!state.alive[i].get()) continue;

            int active = state.active[i].get();
            active = Math.max(1, active);

            long rawLatency;

            int c = state.count[i].get();

            if (c == 0) {
                rawLatency = 1;
            } else {
                rawLatency = state.latency[i].get() / c / 1_000_000;
            }

            rawLatency = Math.max(1, rawLatency);

            if (ewmaLatency[i] == 0) {
                ewmaLatency[i] = rawLatency;
            } else {
                ewmaLatency[i] =
                        ALPHA * rawLatency + (1 - ALPHA) * ewmaLatency[i];
            }

            double score =
                    (active * 1.2) +        // load
                            (ewmaLatency[i] * 0.8); // latency (smoothed)

            System.out.println(
                    "SERVER " + (3001 + i)
                            + " active=" + active
                            + " latency=" + (int) ewmaLatency[i]
                            + " score=" + (int) score
            );

            if (score < bestScore) {
                bestScore = score;
                best = i;
            }
        }

        return best;
    }
}