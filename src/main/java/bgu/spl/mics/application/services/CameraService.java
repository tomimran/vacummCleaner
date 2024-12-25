package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.StatisticalFolder;

import java.util.List;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * 
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {

    private final Camera camera;
    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super("Camera Service");
        this.camera = camera;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (broadcast) -> {
            int tick = broadcast.getTick();
            List<DetectedObject> detectedObjects = camera.detect(tick);
            if (detectedObjects != null) {
                StatisticalFolder.getInstance().addDetectedObjects(detectedObjects.size());
                sendEvent(new DetectObjectsEvent(detectedObjects, tick));
            }
        });
        subscribeBroadcast(TerminatedBroadcast.class, (broadcast) -> {

        });
        subscribeBroadcast(CrashedBroadcast.class, (broadcast) -> {
            terminate();
        });
    }
}
