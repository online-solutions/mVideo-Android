package plus.studio.mvideo.tabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import plus.studio.mvideo.R;
import plus.studio.mvideo.adapters.CustomListAdapter;
import plus.studio.mvideo.logging.L;
import plus.studio.mvideo.models.Movie;
import plus.studio.mvideo.rest.models.ApiResponse;
import plus.studio.mvideo.utils.AppController;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by yohananjr13 on 5/3/2015.
 */

public class Tab1 extends Fragment {

    // Movies json url
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);

        listView = (ListView) v.findViewById(R.id.list);


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        // TODO: change to retrofit
        AppController.getRestClient().getMovieService().getMovies(new Callback<ApiResponse>() {
            @Override
            public void success(ApiResponse apiResponse, retrofit.client.Response response) {
                movieList = apiResponse.getResults();
                adapter = new CustomListAdapter(getActivity(), movieList);
                listView.setAdapter(adapter);
                hidePDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                L.t(getActivity(), "retrofit failure");
                hidePDialog();
            }
        });



        return v;
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
