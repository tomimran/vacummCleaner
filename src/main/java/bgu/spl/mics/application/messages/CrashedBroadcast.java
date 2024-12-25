package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;

public class CrashedBroadcast implements Broadcast {
    private Class<? extends MicroService> type;

    public CrashedBroadcast(Class<? extends MicroService> type) {
        this.type = type;
    }
    public Class<? extends MicroService> getType() {
        return type;
    }
}
