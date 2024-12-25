package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */

public class Camera {

    private final int id;
    private int frequency;
    private STATUS status;
    private List<StampedDetectedObjects> detectedObjectsList;

    public Camera(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
        this.status = STATUS.UP;
        this.detectedObjectsList = new ArrayList<>();
    }

    public List<DetectedObject> detect(int time) {
        for (StampedDetectedObjects detectedObjects : detectedObjectsList) {
            if (detectedObjects.getTime() == time - frequency) {
                return detectedObjects.getDetectedObjects();
            }
        }
        return null;
    }

    public void turnDown() {
        this.status = STATUS.DOWN;
    }

    public void turnUp() {
        this.status = STATUS.UP;
    }

    public void setError() {
        this.status = STATUS.ERROR;
    }
}
