package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
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
    private int clock;
    private DetectObjectsEvent toProcess;
    private int check;
    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker LiDarWorkerTracker) {
        super("LiDar Service");
        tracker = LiDarWorkerTracker;
        toProcess = null;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (broadcast) -> {clock = broadcast.getTick();});
        subscribeBroadcast(TerminatedBroadcast.class, (broadcast) -> {});
        subscribeBroadcast(CrashedBroadcast.class, (broadcast) -> {terminate();});
        subscribeEvent(DetectObjectsEvent.class, (broadcast) -> {toProcess = broadcast;});
    }

    private void process (DetectObjectsEvent event) {

    }

    /*private void receiveTick (TickBroadcast broadcast) {//should implement this as update last detected in the worker class
        if (toProcess != null) {
            List<TrackedObject> updatedList = new ArrayList<>();
            if (toProcess.getTime() + tracker.getFrequency() >= broadcast.getTick()) {
                for (DetectedObject detectedObject : toProcess.getDetectedObjects()) {
                    StampedCloudPoints cloudPoints = LiDarDataBase.getInstance().getCloudPoint(detectedObject.getId(), toProcess.getTime());
                    updatedList.add(new TrackedObject(detectedObject.getId(), toProcess.getTime(), detectedObject.getDescription(), cloudPoints.getCloudPoints()))
                }
            }
        }
    }*/
}
