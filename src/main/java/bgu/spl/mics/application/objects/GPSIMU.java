package bgu.spl.mics.application.objects;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick;
    private STATUS status;
    private List<Pose> poseList;

    public GPSIMU(String filePath) {
        currentTick = 0;
        status = STATUS.UP; //Should be init to what status?
        initializePoseList(filePath);
    }

    public void updateTime(int time){ // not sure this should be public, how can i update the current tick then?
        currentTick = time;
    }

    public Pose getCurrentPose() {
        return poseList.get(currentTick);
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

    private void initializePoseList(String filePath) {
        try {
            // Initialize Gson
            Gson gson = new Gson();

            // Parse JSON file into Configuration object
            FileReader reader = new FileReader(filePath);
            Type type = new TypeToken<List<Pose>>(){}.getType();
            poseList = gson.fromJson(reader, type);
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
