package bgu.spl.mics.application.messages;
import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.Event;
//Sending a pose to FusionSlam
public class PoseEvent implements Event<Boolean> {//What type should it actually return? boolean as default {
    private Pose currentPose;
    private int time;

    public PoseEvent(Pose currentPose, int time) {
        this.currentPose = currentPose;
        this.time = time;
    }

    public int getTime(){
        return time;
    }

    public Pose getCurrentPose(){
        return currentPose;
    }
}
