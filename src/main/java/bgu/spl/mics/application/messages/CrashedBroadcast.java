package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.CameraService;

public class CrashedBroadcast implements Broadcast {

    private final String id;
    private final String description;

    public CrashedBroadcast(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return id + " " + description;
    }
}
