package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {

    private static class SingletonHolder {
        private static final StatisticalFolder INSTANCE = new StatisticalFolder();
    }

    private int systemRuntime;
    private int numDetectedObjects;
    private int numTrackedObjects;
    private int numLandmarks;

    public static StatisticalFolder getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private StatisticalFolder() {
        systemRuntime = 0;
        numDetectedObjects = 0;
        numTrackedObjects = 0;
        numLandmarks = 0;
    }

    public void tick() {
        systemRuntime += 1;
    }

    public void addDetectedObjects(int num) {
        numDetectedObjects += num;
    }

    public void addTrackedObjects(int num) {
        numTrackedObjects += num;
    }

    public void addLandmark() {
        numLandmarks += 1;
    }
}
