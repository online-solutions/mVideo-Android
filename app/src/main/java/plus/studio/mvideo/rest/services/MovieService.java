package plus.studio.mvideo.rest.services;

import plus.studio.mvideo.rest.models.ApiResponse;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by yohananjr13 on 5/11/15.
 */
public interface MovieService {

    @GET("/movies")
    void getMovies(Callback<ApiResponse> callback);
}
