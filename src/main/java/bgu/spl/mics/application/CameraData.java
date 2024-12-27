package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CameraData {

    private ConcurrentHashMap<String, List<StampedDetectedObjects>> data;
    public CameraData(String filePath) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(filePath);
            Type listType = new TypeToken<ConcurrentHashMap<String, List<List<StampedDetectedObjects>>>>() {}.getType();
            ConcurrentHashMap<String, List<List<StampedDetectedObjects>>> temp = gson.fromJson(reader, listType);
            reader.close();

            data = new ConcurrentHashMap<>();
            for (String key : temp.keySet()) {
                data.put(key, temp.get(key).get(0));
            }
            for (List<List<StampedDetectedObjects>> l : temp.values()) {
                for (List<StampedDetectedObjects> list : l) {
                    System.out.println(list.size());
                }
            }

            for (String key : data.keySet()) {
                System.out.println(key + ":");
                for (StampedDetectedObjects obj : data.get(key)) {
                    System.out.println("Time: " + obj.getTime());
                    for (DetectedObject det : obj.getDetectedObjects()) {
                        System.out.println(det.getId() + " " + det.getDescription());
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<StampedDetectedObjects> getStampedDetectedObjects(String id) {
        return data.get(id);
    }
}
