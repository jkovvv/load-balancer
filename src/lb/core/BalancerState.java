package lb.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.*;

public class BalancerState {

    public final List<String> servers;

    public final AtomicInteger[] active;

    public final AtomicBoolean[] alive;

    public final AtomicLong[] latency;

    public final AtomicInteger[] count;

    public final double[] ewmaLatency;


    public BalancerState(List<String> servers) {

        this.servers = new ArrayList<>(servers);

        int size = servers.size();


        this.active = new AtomicInteger[size];

        this.alive = new AtomicBoolean[size];

        this.latency = new AtomicLong[size];

        this.count = new AtomicInteger[size];

        this.ewmaLatency = new double[size];


        for (int i = 0; i < size; i++) {

            active[i] = new AtomicInteger(0);

            alive[i] = new AtomicBoolean(true);

            latency[i] = new AtomicLong(0);

            count[i] = new AtomicInteger(0);

            ewmaLatency[i] = 0;
        }
    }
}