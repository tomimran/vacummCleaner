package bgu.spl.mics.application.objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {
    private List<StampedCloudPoints> cloudPoints;
    private static String filePath = "";
    private static boolean initialized = false;

    private static class LiDarDataBaseHolder {
        private final static LiDarDataBase INSTANCE = new LiDarDataBase(filePath);
    }
    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public static LiDarDataBase getInstance(String filePath) {
        LiDarDataBase.filePath = filePath;
        return LiDarDataBaseHolder.INSTANCE;
    }

    public static LiDarDataBase getInstance() {
        return LiDarDataBaseHolder.INSTANCE;
    }

    private LiDarDataBase(String filePath) {
        cloudPoints = new ArrayList<>();
        //Parsing the filePath
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<StampedCloudPoints>>() {}.getType();
            cloudPoints = gson.fromJson(reader, listType);
        } catch (Exception e) {
            System.err.println("Failed to parse LiDAR data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<StampedCloudPoints> getCloudPoints() {
        return cloudPoints;
    }

    public StampedCloudPoints getCloudPoint(String id, int time) {
        for (StampedCloudPoints cloudPoint : cloudPoints) {
            if (id.equals(cloudPoint.getId()) && cloudPoint.getTime() == time) {
                return cloudPoint;
            }
        }
        return null;
    }
}
