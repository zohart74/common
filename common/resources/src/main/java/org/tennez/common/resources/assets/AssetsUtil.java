package org.tennez.common.resources.assets;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tenne on 3/10/2016.
 */
public class AssetsUtil {

    public static InputStream getAssetAsStream(Context context, String assetPath) throws IOException {
        return context.getAssets().open(assetPath);
    }

    public static String getAssetAsString(Context context, String assetPath) throws IOException {
        InputStream assetStream = getAssetAsStream(context, assetPath);
        if (assetStream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetStream));
        StringBuilder assetStringBuilder = new StringBuilder();
        while (reader.ready()) {
            assetStringBuilder.append(reader.readLine()).append("\n");
        }
        reader.close();
        return assetStringBuilder.toString();
    }

    public static JSONObject getAssetAsJsonObject(Context context, String assetPath) throws IOException, JSONException {
        return new JSONObject(getAssetAsString(context, assetPath));
    }

    public static JSONArray getAssetsAsJsonArray(Context context, String assetsPath, FilenameFilter filter) throws IOException, JSONException {
        String[] assets = context.getAssets().list(assetsPath);
        if (assets == null) {
            return null;
        }
        JSONArray assetsJSONArray = new JSONArray();
        for (int i = 0; i < assets.length; ++i) {
            if (filter == null || filter.accept(new File(assetsPath), assets[i])) {
                assetsJSONArray.put(getAssetAsJsonObject(context, assetsPath + "/" + assets[i]));
            }
        }
        return assetsJSONArray;
    }

    public static JSONArray getAssetsAsJsonArray(Context context, String assetsPath) throws IOException, JSONException {
        return getAssetsAsJsonArray(context, assetsPath, null);
    }
}
