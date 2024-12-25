package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the FusionSLAM service.
 * 
 * This service interacts with the LiDarWorkerTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {

    private LiDarWorkerTracker tracker;
    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker LiDarWorkerTracker) {
        super("LiDar Service");
        tracker = LiDarWorkerTracker;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (broadcast) -> {
            List<TrackedObject> trackedObjects = tracker.getTrackedObjects(broadcast.getTick());
            if (!trackedObjects.isEmpty()) {
                for (TrackedObject trackedObject : trackedObjects) {
                    if (trackedObject.getId().equals("ERROR")) {
                        tracker.setError();
                        sendBroadcast(new CrashedBroadcast("Sensor " + tracker.getId(), trackedObject.getDescription()));
                        terminate();
                    }
                }
                StatisticalFolder.getInstance().addTrackedObjects(trackedObjects.size());
                sendEvent(new TrackedObjectsEvent(trackedObjects, trackedObjects.getFirst().getTime()));
            }
        });
        subscribeBroadcast(TerminatedBroadcast.class, (broadcast) -> {
            if (broadcast.getType().equals(TimeService.class) | broadcast.getType().equals(FusionSlamService.class)) {
                sendBroadcast(new TerminatedBroadcast(this.getClass()));
                tracker.turnDown();
                terminate();
            }
        });
        subscribeBroadcast(CrashedBroadcast.class, (broadcast) -> {
            terminate();
        });
        subscribeEvent(DetectObjectsEvent.class, (event) -> {
            tracker.updateTrackedObjects(event.getTime(), event.getDetectedObjects());
        });
    }
}
