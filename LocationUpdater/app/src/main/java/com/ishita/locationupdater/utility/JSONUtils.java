package com.ishita.locationupdater.utility;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishita.locationupdater.model.LocationInformation;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    private static final String fileName = "location_data.json";

    /**
     * Method to save list data into json file
     *
     * @param context context
     * @param info    List of location info
     */
    public static void saveData(Context context, ArrayList<LocationInformation> info) {
        String jsonString = new Gson().toJson(info);
        try {
            Log.d(Category.CATEGORY_GENERAL, "JSONUtils saveData mJsonResponse \n" + jsonString);
            File jsonFile = new File(context.getFilesDir().getPath() + "/" + fileName);
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
            FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + fileName);
            file.write(jsonString);
            file.flush();
            file.close();
            EventModel model = new EventModel(EventNotifierConstants.NOTIFIER_TYPE_LOCATION, info);
            EventDispatcher.getInstance().dispatchEvent(EventTypes.NEW_LOCATION_LOGGED, model);
            NotificationUtils.showNotification(context,info.get(info.size()-1));
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getMessage());
        }
    }



    /**
     * Method that will read previously written data from json
     *
     * @param context
     * @return json String
     */
    public static String loadDataFromJSON(Context context) {
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to convert json String into {@link ArrayList} of {@link LocationInformation}
     *
     * @param context
     * @return
     */
    public static ArrayList<LocationInformation> getArrayListFromJSONData(Context context) {
        String jsonData = getData(context);
        Log.d(Category.CATEGORY_GENERAL, "JSONUtils writeListToJSONFile jsonData \n" + jsonData);
        try {
            return new Gson().fromJson(jsonData, new TypeToken<List<LocationInformation>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * method to create new json file for storing data
     * @param context Context
     * @return returns  true if file is created successfully false otherwise
     */
    public static boolean create(Context context) {
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write("[]".getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getMessage());
        }
        return false;
    }

    /**
     * method to check whether file already exists
     * @param context context
     * @return returns true if file is present false otherwise
     */
    public static boolean isFilePresent(Context context) {
        File f = new File(context.getFilesDir().getPath() + "/" + fileName);
        return f.exists();
    }

    private static String getData(Context context) {
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getMessage());
            return null;
        }
    }
}
