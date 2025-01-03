package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;

import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
/**
 * The main entry point for the GurionRock Pro Max Ultra Over 9000 simulation.
 * <p>
 * This class initializes the system and starts the simulation by setting up
 * services, objects, and configurations.
 * </p>
 */
public class GurionRockRunner {

    /**
     * The main method of the simulation.
     * This method sets up the necessary components, parses configuration files,
     * initializes services, and starts the simulation.
     *
     * @param args Command-line arguments. The first argument is expected to be the path to the configuration file.
     */
    public static void main(String[] args) {

        /// Parse of the config files:
        /*if (args.length < 1) {
            System.out.println("Error: Path to the JSON file must be provided as a command-line argument.");
            return;
        }*/

        //String jsonFilePath = args[0];
        String jsonFilePath = "example input/configuration_file.json"; //should receive this from args
        Configuration config = null;
        try {
            // Initialize Gson
            Gson gson = new Gson();

            // Parse JSON file into Configuration object
            FileReader reader = new FileReader(jsonFilePath);
            config = gson.fromJson(reader, Configuration.class);
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        List<Camera> cameras = config.Cameras;
        List<LiDarWorkerTracker> lidarWorkerTrackers = config.Lidars.LidarConfigurations;
        String lidarDataPath = config.Lidars.lidars_data_path;
        String poseJsonFile = config.poseJsonFile;
        int TickTime = config.TickTime;
        int Duration = config.Duration;

        System.out.println("init finish");
        /// Initialization of the system:
        for (Camera camera : cameras) {
            CameraService cameraService = new CameraService(camera);
            Thread thread = new Thread(cameraService);
            thread.start();
        }
        System.out.println("cameras started");
        for (LiDarWorkerTracker lidarWorkerTracker : lidarWorkerTrackers) {
            LiDarService lidarService = new LiDarService(lidarWorkerTracker);
            Thread thread = new Thread(lidarService);
            thread.start();
        }
        FusionSlamService fusionSlamService = new FusionSlamService(FusionSlam.getInstance());
        Thread thread = new Thread(fusionSlamService);
        thread.start();
        LiDarDataBase.getInstance(lidarDataPath);
        PoseService poseService = new PoseService(new GPSIMU(poseJsonFile));
        Thread thread2 = new Thread(poseService);
        thread2.start();
        TimeService timeService = new TimeService(TickTime, Duration);
        Thread thread3 = new Thread(timeService);
        thread3.start(); //here the simulation starts
    }

    private class Configuration {
        private List<Camera> Cameras;
        private Lidars Lidars;
        private String poseJsonFile;
        private int TickTime;
        private int Duration;

        private List<Camera> getCameras() {
            return Cameras;
        }

        private Lidars getLidars() {
            return Lidars;
        }

        private String getPoseJsonFile() {
            return poseJsonFile;
        }

        private int getTickTime() {
            return TickTime;
        }

        private int getDuration() {
            return Duration;
        }
    }

    private class Lidars {
        private List<LiDarWorkerTracker> LidarConfigurations;
        private String lidars_data_path;

        private List<LiDarWorkerTracker> getLidarWorkerTrackers() {
            return LidarConfigurations;
        }
        private String getLidarDataPath() {
            return lidars_data_path;
        }
    }
}
