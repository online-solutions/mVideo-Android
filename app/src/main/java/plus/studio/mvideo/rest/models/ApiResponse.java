package plus.studio.mvideo.rest.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import plus.studio.mvideo.models.Movie;

/**
 * Created by yohananjr13 on 5/11/15.
 */
public class ApiResponse {
    @SerializedName("results")
    private ArrayList<Movie> results;

    public ArrayList<Movie> getResults() {
        return results;
    }
}
