package net.usrlib.android.popularmovies.movieapi;

import org.json.JSONObject;

/**
 * Copyright 2016 rgr-myrg
 * <p/>
 * Licensed under the Apache License:
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Created by rgr-myrg on 2/24/16 6:17 PM.
 */
public class MovieItemVO {

	private String originalTitle;
	private String posterPath;
	private String imageUrl;
	private String overview;
	private String releaseDate;

	private int voteCount;
	private double voteAverage;

	public String getOriginalTitle() {
		return originalTitle;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getOverview() {
		return overview;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public int getVoteCount() {
		return voteCount;
	}

	public Number getVoteAverage() {
		return voteAverage;
	}

	public static MovieItemVO fromJsonObject(JSONObject jsonObject) {
		MovieItemVO vo = new MovieItemVO();

		if (jsonObject != null) {
			vo.originalTitle = jsonObject.optString(MovieVars.ORIGINAL_TITLE, MovieVars.UNSET_VALUE);
			vo.posterPath    = jsonObject.optString(MovieVars.POSTER_PATH, MovieVars.UNSET_VALUE);
			vo.overview      = jsonObject.optString(MovieVars.OVERVIEW, MovieVars.UNSET_VALUE);
			vo.releaseDate   = jsonObject.optString(MovieVars.RELEASE_DATE, MovieVars.UNSET_VALUE);
			vo.voteCount     = jsonObject.optInt(MovieVars.VOTE_COUNT, 0);
			vo.voteAverage   = jsonObject.optDouble(MovieVars.VOTE_AVERAGE, 0);
			vo.imageUrl      = MovieVars.IMG_BASE_URL + vo.posterPath;
		}

		return vo;
	}

}

/*
poster_path	String	/inVq3FRqcYIRl2la8iZikYYxFNR.jpg
adult	Boolean	false
overview	String	Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.
release_date	String	2016-02-09
genre_ids	Array
id	Integer	293660
original_title	String	Deadpool
original_language	String	en
title	String	Deadpool
backdrop_path	String	/n1y094tVDFATSzkTnFxoGZ1qNsG.jpg
popularity	Number	75.204505
vote_count	Integer	1239
video	Boolean	false
vote_average	Number	7.31
 */