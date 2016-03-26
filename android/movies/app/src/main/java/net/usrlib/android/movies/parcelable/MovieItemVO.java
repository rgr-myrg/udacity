package net.usrlib.android.movies.parcelable;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import net.usrlib.android.movies.data.MoviesContract.FavoritesEntry;
import net.usrlib.android.movies.movieapi.MovieVars;
import net.usrlib.android.util.MathUtil;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class MovieItemVO implements Parcelable {

	public static final String NAME = MovieItemVO.class.getSimpleName();

	private int id;
	private String originalTitle;
	private String posterPath;
	private String imageUrl;
	private String overview;
	private String releaseDate;

	private int voteCount;
	private double voteAverage;
	private double popularity;

	public MovieItemVO(
			int id,
			String originalTitle,
			String posterPath,
			String overview,
			String releaseDate,
			int voteCount,
			double voteAverage,
			double popularity) {

		this.id = id;
		this.originalTitle = originalTitle;
		this.posterPath = posterPath;
		this.overview = overview;
		this.releaseDate = releaseDate;
		this.voteCount = voteCount;
		this.voteAverage = voteAverage;
		this.popularity = popularity;

		// themoviedb api sometimes contains a null string value as a data point.
		if (posterPath.contentEquals("null")) {
			this.imageUrl = MovieVars.PLACE_HOLDER;
		} else {
			this.imageUrl = MovieVars.IMG_BASE_URL + posterPath;
		}
	}

	public MovieItemVO(Parcel parcel) {
		String[] data = new String[9];

		parcel.readStringArray(data);

		this.id = Integer.valueOf(data[0]);
		this.originalTitle = data[1];
		this.posterPath = data[2];
		this.overview = data[3];
		this.releaseDate = data[4];
		this.voteCount = Integer.valueOf(data[5]);
		this.voteAverage = Double.valueOf(data[6]);
		this.popularity = Double.valueOf(data[7]);
		this.imageUrl = data[8];
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{
				String.valueOf(this.id),
				this.originalTitle,
				this.posterPath,
				this.overview,
				this.releaseDate,
				String.valueOf(this.voteCount),
				String.valueOf(this.voteAverage),
				String.valueOf(this.popularity),
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

	public final int getId() {
		return id;
	}

	public final String getOriginalTitle() {
		return originalTitle;
	}

	public final String getPosterPath() {
		return posterPath;
	}

	public final String getImageUrl() {
		return imageUrl;
	}

	public final String getOverview() {
		return overview;
	}

	public final String getReleaseDate() {
		return releaseDate;
	}

	public final int getVoteCount() {
		return voteCount;
	}

	public final Number getVoteAverage() {
		return voteAverage;
	}

	public final double getPopularity() {
		return popularity;
	}

	public final static MovieItemVO fromJsonObject(final JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}

		// Round vote count to the nearest decimal
		final double voteCount = MathUtil.round(
				jsonObject.optDouble(FavoritesEntry.COLUMN_VOTE_AVERAGE, 0),
				1
		);

		String releaseDate = jsonObject.optString(
				FavoritesEntry.COLUMN_RELEASE_DATE,
				MovieVars.UNSET_VALUE
		);

		// Format to friendly date. Ex: 2016-02-09 to February 10, 2016
		try {
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			final SimpleDateFormat newFormat  = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
			final Date date = dateFormat.parse(releaseDate);
			releaseDate = newFormat.format(date);

		} catch (ParseException e) {
			releaseDate = MovieVars.UNSET_VALUE;
			e.printStackTrace();
			Log.e(NAME, jsonObject.toString());
		}

		return new MovieItemVO(
				jsonObject.optInt(FavoritesEntry.COLUMN_ID, 0),
				jsonObject.optString(FavoritesEntry.COLUMN_ORIGINAL_TITLE, MovieVars.UNSET_VALUE),
				jsonObject.optString(FavoritesEntry.COLUMN_POSTER_PATH, MovieVars.UNSET_VALUE),
				jsonObject.optString(FavoritesEntry.COLUMN_OVERVIEW, MovieVars.UNSET_VALUE),
				releaseDate,
				jsonObject.optInt(FavoritesEntry.COLUMN_VOTE_COUNT, 0),
				voteCount,
				jsonObject.optDouble(FavoritesEntry.COLUMN_POPULARITY, 0)
		);
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(FavoritesEntry.COLUMN_ID, id);
		values.put(FavoritesEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
		values.put(FavoritesEntry.COLUMN_POSTER_PATH, posterPath);
		values.put(FavoritesEntry.COLUMN_OVERVIEW, overview);
		values.put(FavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);
		values.put(FavoritesEntry.COLUMN_VOTE_COUNT, voteCount);
		values.put(FavoritesEntry.COLUMN_VOTE_AVERAGE, voteAverage);
		values.put(FavoritesEntry.COLUMN_POPULARITY, popularity);

		return values;
	}

}

// Sample data
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