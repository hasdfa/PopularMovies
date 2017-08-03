package com.vadim.hasdfa.udacity.favorite_movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raksha Vadim on 29.07.17, 22:06.
 */

public class Movie implements Parcelable {
    private boolean isAd = false;

    private String posterPath;
    private boolean adult;
    private String overview;
    private String releaseDate;
    private int id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private double popularity;
    private int voteCount;
    private String video;
    private double voteAverage;
    private boolean isFavorite = false;

    public Movie() {

    }
    protected Movie(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = in.readString();
        voteAverage = in.readDouble();
        isFavorite = in.readByte() != 0;
        isAd = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        Movie m = (Movie) obj;
        boolean isEqual = false;
        try {
            isEqual = this.posterPath.equals(m.posterPath) &&
                    this.adult == m.adult &&
                    this.overview.equals(m.overview) &&
                    this.releaseDate.equals(m.releaseDate) &&
                    this.id == m.id &&
                    this.originalTitle.equals(m.originalTitle) &&
                    this.originalLanguage.equals(m.originalLanguage) &&
                    this.title.equals(m.title) &&
                    this.backdropPath.equals(m.backdropPath) &&
                    this.popularity == m.popularity &&
                    this.voteCount == m.voteCount &&
                    this.video.equals(m.video) &&
                    this.voteAverage == m.voteAverage &&
                    this.isAd == m.isAd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEqual;
    }

    @Override
    public String toString() {
        return title + ";isFavorite:"+isFavorite;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeString(originalLanguage);
        parcel.writeString(title);
        parcel.writeString(backdropPath);
        parcel.writeDouble(popularity);
        parcel.writeInt(voteCount);
        parcel.writeString(video);
        parcel.writeDouble(voteAverage);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeByte((byte) (isAd ? 1 : 0));
    }

    public static Movie ad() {
        Movie m = new Movie();
        m.setAd(true);
        return m;
    }
}
