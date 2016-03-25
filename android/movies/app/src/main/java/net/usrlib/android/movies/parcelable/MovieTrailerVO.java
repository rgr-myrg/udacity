package net.usrlib.android.movies.parcelable;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import net.usrlib.android.movies.movieapi.MovieVars;

import org.json.JSONObject;

public class MovieTrailerVO implements Parcelable {

	public static final String NAME = MovieTrailerVO.class.getSimpleName();

	public static final String ID_KEY = "id";
	public static final String NAME_KEY = "name";
	public static final String SITE_KEY = "site";
	public static final String TYPE_KEY = "type";
	public static final String KEY_KEY  = "key";
	public static final String SIZE_KEY = "size";

	private int id;
	private String name;
	private String site;
	private String type;
	private String key;
	private int size;

	private String youtubeUrl;

	public MovieTrailerVO(int id, String name, String site, String type, String key, int size) {
		this.id = id;
		this.name = name;
		this.site = site;
		this.type = type;
		this.key  = key;
		this.size = size;

		if (site.contentEquals(MovieVars.YOUTUBE_SITE)) {
			//https://www.youtube.com/watch?v=8hP9D6kZseM
			this.youtubeUrl = MovieVars.YOUTUBE_URL + key;
		}
	}

	public MovieTrailerVO(Parcel parcel) {
		String[] data = new String[6];

		parcel.readStringArray(data);

		this.id = Integer.valueOf(data[0]);
		this.name = data[1];
		this.site = data[2];
		this.type = data[3];
		this.key  = data[4];
		this.size = Integer.valueOf(data[5]);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{
				String.valueOf(this.id),
				this.name,
				this.site,
				this.type,
				this.key,
				String.valueOf(this.size)
		});
	}

	public static final Creator<MovieTrailerVO> CREATOR = new Creator<MovieTrailerVO>() {
		@Override
		public MovieTrailerVO createFromParcel(Parcel parcel) {
			return new MovieTrailerVO(parcel);
		}

		@Override
		public MovieTrailerVO[] newArray(int size) {
			return new MovieTrailerVO[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSite() {
		return site;
	}

	public String getType() {
		return type;
	}

	public String getKey() {
		return key;
	}

	public int getSize() {
		return size;
	}

	public String getYoutubeUrl() {
		return youtubeUrl;
	}

	public static MovieTrailerVO fromJsonObject(final JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}

		return new MovieTrailerVO(
				jsonObject.optInt(ID_KEY, 0),
				jsonObject.optString(NAME_KEY, MovieVars.UNSET_VALUE),
				jsonObject.optString(SITE_KEY, MovieVars.UNSET_VALUE),
				jsonObject.optString(TYPE_KEY, MovieVars.UNSET_VALUE),
				jsonObject.optString(KEY_KEY, MovieVars.UNSET_VALUE),
				jsonObject.optInt(SIZE_KEY, 0)
		);
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(ID_KEY, id);
		values.put(NAME_KEY, name);
		values.put(SITE_KEY, site);
		values.put(TYPE_KEY, type);
		values.put(KEY_KEY, key);
		values.put(SIZE_KEY, size);

		return values;
	}

}
/*
results	Array
[0]	Object
id	String	533ec689c3a3685448003a58
iso_639_1	String	en
iso_3166_1	String	US
key	String	8hP9D6kZseM
name	String	Trailer 1
site	String	YouTube
size	Integer	720
type	String	Trailer
 */

