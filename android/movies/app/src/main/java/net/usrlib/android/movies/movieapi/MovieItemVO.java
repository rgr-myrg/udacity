package net.usrlib.android.movies.movieapi;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MovieItemVO implements Parcelable {

	private String originalTitle;
	private String posterPath;
	private String imageUrl;
	private String overview;
	private String releaseDate;

	private int voteCount;
	private double voteAverage;

	public static final String NAME = MovieItemVO.class.getSimpleName();

	public MovieItemVO(
			String originalTitle,
			String posterPath,
			String overview,
			String releaseDate,
			int voteCount,
			double voteAverage) {

		this.originalTitle = originalTitle;
		this.posterPath = posterPath;
		this.overview = overview;
		this.releaseDate = releaseDate;
		this.voteCount = voteCount;
		this.voteAverage = voteAverage;
		this.imageUrl = MovieVars.IMG_BASE_URL + posterPath;
	}

	public MovieItemVO(Parcel parcel) {
		String[] data = new String[6];

		parcel.readStringArray(data);

		this.originalTitle = data[0];
		this.posterPath = data[1];
		this.overview = data[2];
		this.releaseDate = data[3];
		this.voteCount = Integer.valueOf(data[4]);
		this.voteAverage = Double.valueOf(data[5]);
		this.imageUrl = MovieVars.IMG_BASE_URL + this.posterPath;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{
				this.originalTitle,
				this.posterPath,
				this.overview,
				this.releaseDate,
				String.valueOf(this.voteCount),
				String.valueOf(this.voteAverage),
				this.imageUrl
		});
	}

	public static final Creator<MovieItemVO> CREATOR = new Creator<MovieItemVO>() {
		@Override
		public MovieItemVO createFromParcel(Parcel parcel) {
			return new MovieItemVO(parcel);
		}

		@Override
		public MovieItemVO[] newArray(int size) {
			return new MovieItemVO[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

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
		if (jsonObject == null) {
			return null;
		}

		return new MovieItemVO(
				jsonObject.optString(MovieVars.ORIGINAL_TITLE, MovieVars.UNSET_VALUE),
				jsonObject.optString(MovieVars.POSTER_PATH, MovieVars.UNSET_VALUE),
				jsonObject.optString(MovieVars.OVERVIEW, MovieVars.UNSET_VALUE),
				jsonObject.optString(MovieVars.RELEASE_DATE, MovieVars.UNSET_VALUE),
				jsonObject.optInt(MovieVars.VOTE_COUNT, 0),
				jsonObject.optDouble(MovieVars.VOTE_AVERAGE, 0)
		);
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