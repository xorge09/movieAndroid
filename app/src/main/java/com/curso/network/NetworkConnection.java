package com.curso.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.curso.listeners.NetworkConnectionInterface;
import com.curso.movieandroid.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xorge on 11/03/2017.
 */

public class NetworkConnection extends AsyncTask<Void, Void, Boolean>{

    private final String TAG = NetworkConnection.class.getSimpleName();
    private Context context;
    private String responseStr;
    private NetworkConnectionInterface listener;

    public NetworkConnection(Context context, NetworkConnectionInterface networkConnectionInterface){
        this.context = context;
        this.listener = networkConnectionInterface;
    }

    /**EL paramentro de los 3 puntos (...) significa que es un Array en Android*/
    @Override
    protected Boolean doInBackground(Void... params) {
        final String BASE_URL = "http://api.themoviedb.org/3/movie";
        final String POPULAR_PATH = "popular";
        final String API_KEY_PARAM = "api_key";

        //Construccion de URL (Uri)
        Uri uriToAPI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(POPULAR_PATH)
                .appendQueryParameter(API_KEY_PARAM, context.getString(R.string.api_key_value))
                .build();
        Log.d(TAG,uriToAPI.toString());

        HttpURLConnection urlConnection;
        BufferedReader reader;

        try {
            URL url = new URL(uriToAPI.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            //Validación a no conección al servidor de la API
            if(inputStream == null){
                return false; //No hay nada que hacer
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            //Lectura del InputStream
            while ((line = reader.readLine())!= null){
                buffer.append(line + "\n"); // Se anexa linea
            }
            if (buffer.length() == 0){
                return false; //No tienen lineas
            }

            responseStr = buffer.toString();
            Log.d(TAG, "Server Response: "+ responseStr);
            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
            return  false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result){
            if (listener != null){
                listener.OnSuccessfullyResponse(responseStr);
            }
        }else {
            if (listener != null){
                listener.OnFailedResponse();
            }
        }
    }
}
