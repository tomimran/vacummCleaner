package bgu.spl.mics.application.objects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {

    private static class SingletonHolder {
        private static final StatisticalFolder INSTANCE = new StatisticalFolder();
    }

    private AtomicInteger systemRuntime;
    private AtomicInteger numDetectedObjects;
    private AtomicInteger numTrackedObjects;
    private AtomicInteger numLandmarks;
    private AtomicInteger activeServices;

    public static StatisticalFolder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private StatisticalFolder() {
        systemRuntime = new AtomicInteger(0);
        numDetectedObjects = new AtomicInteger(0);
        numTrackedObjects = new AtomicInteger(0);
        numLandmarks = new AtomicInteger(0);
        activeServices = new AtomicInteger(0);
    }

    public void tick() {
        systemRuntime.incrementAndGet();
    }

    public void addDetectedObjects(int num) {
        numDetectedObjects += num;
    }

    public void addTrackedObjects(int num) {
        numTrackedObjects.addAndGet(num);
    }

    public void addLandmark() {
        numLandmarks.incrementAndGet();
    }
}
