package com.curso.movieandroid;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.curso.json_utilities.JsonParser;
import com.curso.listeners.NetworkConnectionInterface;
import com.curso.models.Movie;
import com.curso.network.NetworkConnection;

import java.util.ArrayList;

import adapter.MoviesAdapter;

public class MainActivity extends AppCompatActivity implements NetworkConnectionInterface {

    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (RecyclerView)findViewById(R.id.lista_Peliculas);
        lista.setLayoutManager(new GridLayoutManager(this,2));
        lista.setHasFixedSize(true);

        NetworkConnection connection = new NetworkConnection(this, this);
        connection.execute();

        //Creación de una URI
        /***
        uriExample = Uri.parse("http://www.google.com").buildUpon()
                .appendPath("users")
                .appendPath("params")
                .appendQueryParameter("id","carzzo34007")
                .appendQueryParameter("name","Ernesto")
                .build(); ***/
    }

    @Override
    public void OnSuccessfullyResponse(String response) {
        ArrayList<Movie> peliculas = JsonParser.getsMovies(this,response);
        MoviesAdapter adapter = new MoviesAdapter(this,peliculas);
        lista.setAdapter(adapter);
    }

    @Override
    public void OnFailedResponse() {

    }
}
