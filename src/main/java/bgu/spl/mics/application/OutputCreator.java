package bgu.spl.mics.application;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.objects.LandMark;
import bgu.spl.mics.application.objects.StatisticalFolder;

public class OutputCreator {
    private StatisticalFolder stats;
    private List<LandMark> map;
    private CrashedBroadcast error;

    public OutputCreator(StatisticalFolder stats, List<LandMark> map) {
        this.stats = stats;
        this.map = map;
        this.error = null;
    }

    public OutputCreator(StatisticalFolder stats, List<LandMark> map, CrashedBroadcast error) {
        this.stats = stats;
        this.map = map;
        this.error = error;
    }

    public StatisticalFolder getStats() {
        return stats;
    }
    public List<LandMark> getMap() {
        return map;
    }
    public CrashedBroadcast getError() {
        return error;
    }
}
