package plus.studio.mvideo.adapters;

/**
 * Created by yohananjr13 on 5/4/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import plus.studio.mvideo.R;
import plus.studio.mvideo.activities.VideoDetailActivity;
import plus.studio.mvideo.logging.L;
import plus.studio.mvideo.models.Movie;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;

    public CustomListAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);


        // TODO: replace by Picasso
        ImageView thumbnail = (ImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        ImageButton moreInfo = (ImageButton) convertView.findViewById(R.id.more_info);

        // getting App data for the row
        Movie movie = movieItems.get(position);

        // thumbnail image
        Picasso.with(activity).load(movie.getThumbnailUrl()).into(thumbnail);

        // title
        title.setText(movie.getTitle());

        // rating
        rating.setText("Rating: " + String.valueOf(movie.getRating()));

        //duration
        duration.setText(movie.getDuration());

        // genre
//        String genreStr = "";
//        for (String str : movie.getGenre()) {
//            genreStr += str + ", ";
//        }
//        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
//                genreStr.length() - 2) : genreStr;
//        genre.setText(genreStr);

        // release year
        year.setText(String.valueOf(movie.getYear()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.t(activity, position + "");
                Intent intent = new Intent(activity, VideoDetailActivity.class);
                intent.putExtra("movieTitle", movieItems.get(position).getTitle());
                activity.startActivity(intent);
            }
        });

        // click to show menu more info
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(activity, v);
                popupMenu.inflate(R.menu.menu_list_item);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_favorite:
                                L.t(activity, "favorite at " + position);
                                return true;
                            case R.id.view_full_screen:
                                return true;
                            case R.id.download:
                                return true;
                            case R.id.share:
                                return true;
                        }

                        return false;
                    }
                });

                popupMenu.show();


            }
        });

        return convertView;
    }

}
