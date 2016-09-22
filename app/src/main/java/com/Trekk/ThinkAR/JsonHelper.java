package com.Trekk.ThinkAR;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by timgrable1 on 9/22/16.
 */

public class JsonHelper {

    public String loadDataFromAsset(Context context, String jsonFile) {
        String json = null;

        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(jsonFile);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
