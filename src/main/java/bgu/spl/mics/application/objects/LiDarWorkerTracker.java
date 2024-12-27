package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {

    private int id;
    private int frequency;
    private STATUS status;
    private List<TrackedObject> lastTrackedObjects;

    public LiDarWorkerTracker(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
        this.lastTrackedObjects = new ArrayList<>();
    }

    public int getFrequency() {
        return frequency;
    }

    public void updateTrackedObjects(int time, List<DetectedObject> detectedObjects) {
        for (DetectedObject detectedObject : detectedObjects) {
            StampedCloudPoints points = LiDarDataBase.getInstance().getCloudPoint(detectedObject.getId(), time);
            lastTrackedObjects.add(new TrackedObject(detectedObject.getId(), time, detectedObject.getDescription(), convertToCloudPoints(points.getCloudPoints())));
        };
    }

    public List<TrackedObject> getTrackedObjects(int time) {
        List<TrackedObject> trackedObjects = new ArrayList<TrackedObject>();
        for (TrackedObject trackedObject : lastTrackedObjects) {
            if (trackedObject.getTime() + frequency == time) {
                trackedObjects.add(trackedObject);
                lastTrackedObjects.remove(trackedObject);
            }
        }
        return trackedObjects;
    }

    private List<CloudPoint> convertToCloudPoints(List<List<Double>> points) {
        List<CloudPoint> toReturn = new ArrayList<>();
        for (List<Double> point : points) {
            if (point.size() >= 2) {
                toReturn.add(new CloudPoint(point.get(0), point.get(1)));
            }
        }
        return toReturn;
    }
}
