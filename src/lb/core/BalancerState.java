package lb.core;

import java.util.concurrent.atomic.*;

public class BalancerState {

    public final double[] ewmaLatency = new double[3];

    public final String[] servers = {
            "http://server1:3001",
            "http://server2:3002",
            "http://server3:3003"
    };

    public final AtomicInteger[] active = {
            new AtomicInteger(0),
            new AtomicInteger(0),
            new AtomicInteger(0)
    };

    public final AtomicBoolean[] alive = {
            new AtomicBoolean(true),
            new AtomicBoolean(true),
            new AtomicBoolean(true)
    };

    public final AtomicLong[] latency = {
            new AtomicLong(0),
            new AtomicLong(0),
            new AtomicLong(0)
    };

    public final AtomicInteger[] count = {
            new AtomicInteger(0),
            new AtomicInteger(0),
            new AtomicInteger(0)
    };
}
