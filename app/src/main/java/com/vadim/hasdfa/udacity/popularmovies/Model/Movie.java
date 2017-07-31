package com.vadim.hasdfa.udacity.popularmovies.Model;

/**
 * Created by Raksha Vadim on 29.07.17, 22:06.
 */

public class Movie {
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
                    this.voteAverage == m.voteAverage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEqual;
    }

    @Override
    public String toString() {
        return title;
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
}
