package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;

import java.util.List;

public class DetectObjectsEvent implements Event<Boolean> {
    private List<DetectedObject> detectedObjects;
    private int time;

    public DetectObjectsEvent(List<DetectedObject> detectedObjects, int time) {
        this.detectedObjects = detectedObjects;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public List<DetectedObject> getDetectedObjects() {
        return detectedObjects;
    }
}
