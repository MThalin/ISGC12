package com.example.labb4;

import android.content.Context;
import android.util.Log;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class DataModel {

    private ArrayList<String> listTemp;
    public ArrayList<String> listFav;
    private String movie;
    private String myURL;
    private Context cont;
    private final String FILENAME = "movieSave.txt";

    RequestQueue requestQueue;
    Cache cache;
    Network network;

    public DataModel (Context contIn) {

        cont = contIn;
        listFav = new ArrayList<>();
        cache = new DiskBasedCache(cont.getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public void getContaining(String title) {

        movie = title.replace(" ", "+");
        myURL = "https://www.myapifilms.com/imdb/idIMDB?title="+movie+"&token=4792efeb-357f-4b41-a9f5-1f977bb3a7b8&format=json&limit=10&filter=3";
        listTemp = new ArrayList<>();

        JsonObjectRequest ContainRequest = new JsonObjectRequest
                (Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("labb4", response.toString());

                        try {

                            JSONObject mainObject = response.getJSONObject("data");
                            JSONArray movieArray = mainObject.getJSONArray("movies");

                            for (int i = 0; i < 10; i++) {

                                JSONObject wObject = movieArray.getJSONObject(i);
                                String tempTitle = wObject.get("title").toString();
                                Log.d("labb4","Found: " +tempTitle);
                                listTemp.add(tempTitle);
                            }

                            MainActivity.refreshMain(listTemp);
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }}, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("labb4", "Error occurred: "+error);
                    }
                });

        requestQueue.add(ContainRequest);
    }

    public void getSimilar(String title) {

        movie = title.replace(" ", "+");
        myURL = "https://www.myapifilms.com/imdb/idIMDB?title="+movie+"&token=4792efeb-357f-4b41-a9f5-1f977bb3a7b8&format=json&limit=1&filter=3&similarMovies=1";
        listTemp = new ArrayList<>();

        JsonObjectRequest SimilarRequest = new JsonObjectRequest
                (Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("labb4", response.toString());

                        try {

                            JSONObject mainObject = response.getJSONObject("data");
                            JSONArray movieArray = mainObject.getJSONArray("movies");
                            JSONObject subObject = movieArray.getJSONObject(0);
                            JSONArray simArray = subObject.getJSONArray("similarMovies");

                            for (int i = 0; i < simArray.length(); i++) {

                                JSONObject wObject = simArray.getJSONObject(i);
                                String tempTitle = wObject.get("name").toString();
                                Log.d("labb4","Found: " +tempTitle);
                                listTemp.add(tempTitle);
                            }

                            PopupActivity.refreshPopup(listTemp);
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("labb4", "Error occurred: "+error);
                    }
                });

        requestQueue.add(SimilarRequest);
    }

    public ArrayList<String> getFavList() {
        return listFav;
    }

    public void emptyFavList() {
        listFav.clear();
    }

    public void writeToFile() {

        try {

            FileOutputStream outStream = cont.openFileOutput(FILENAME, MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(outStream);
            output.writeObject(listFav);
            output.close();
            outStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("labb4", "wrote to file: " + String.valueOf(listFav));
    }

    public void readFromFile() {

        try {

            FileInputStream inStream = cont.openFileInput(FILENAME);

            if (inStream != null) {

                ObjectInputStream input = new ObjectInputStream(inStream);
                listFav = (ArrayList<String>) input.readObject();
                input.close();
            }
            inStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("labb4", "read from file: " + String.valueOf(listFav));
    }
}
