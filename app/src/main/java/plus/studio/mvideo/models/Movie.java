package plus.studio.mvideo.models;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

/**
 * Created by yohananjr13 on 5/4/15.
 */
@Parcel
public class Movie {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String title;

    @SerializedName("icon")
    private String thumbnailUrl;

    @SerializedName("release")
    private int year;

    @SerializedName("rating")
    private double rating;

    @SerializedName("duration")
    private String duration;

    public Movie() {
    }

    public Movie(String name, String thumbnailUrl, int year, double rating) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
