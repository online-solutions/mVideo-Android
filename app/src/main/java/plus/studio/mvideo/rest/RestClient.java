package plus.studio.mvideo.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import plus.studio.mvideo.rest.services.MovieService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by yohananjr13 on 5/11/15.
 */
public class RestClient {
    private static final String BASE_URL = "http://mvideo.herokuapp.com/api/";
    private MovieService movieService;

    public RestClient(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        movieService = restAdapter.create(MovieService.class);
    }

    public MovieService getMovieService(){
        return movieService;
    }
}
