package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.CameraData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */

public class Camera {

    private final int id;
    private int frequency;
    @SerializedName("camera_key")
    private String cameraKey;
    private STATUS status;
    private List<StampedDetectedObjects> detectedObjectsList;

    public Camera(int id, int frequency, String camera_data_path, String camera_key) {
        this.id = id;
        this.frequency = frequency;
        this.cameraKey = camera_key;
        this.status = STATUS.UP;
        this.detectedObjectsList = new CameraData(camera_data_path).getStampedDetectedObjects(camera_key);
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

    public String getCameraKey() {
        return cameraKey;
    }

    public int getId() {
        return id;
    }
}
