package plus.studio.mvideo.utils;

/**
 * Created by yohananjr13 on 5/4/15.
 */

import android.app.Application;

import plus.studio.mvideo.rest.RestClient;

public class AppController extends Application {

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        restClient = new RestClient();
    }

    public static RestClient getRestClient() {
        return restClient;
    }
}

