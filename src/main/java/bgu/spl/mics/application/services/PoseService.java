package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.GPSIMU;

/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
//Responsibilities:
//o Holds the robot's coordinates at each tick.
//o Sends PoseEvents.
//o Subscribes to TickBroadcast.
public class PoseService extends MicroService {
    private GPSIMU gpsimu;
    private int clock;
    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("Pose Service"); //How do I know what to name it?
        //What does the gpsimu do? how do i use it to get the coordinates at each tick? where do i send the message - under what logic?
        // TODO Implement this
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (broadcast) -> {
            clock = broadcast.getTick();
        });
        subscribeBroadcast(TerminatedBroadcast.class, (broadcast) -> {});
        subscribeBroadcast(CrashedBroadcast.class, (broadcast) -> {terminate();});
    }
}
