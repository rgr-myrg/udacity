package net.usrlib.android.movies.movieapi;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MovieReviewVO implements Parcelable {

	public static final String ID_KEY = "id";
	public static final String AUTHOR_KEY = "author";
	public static final String CONTENT_KEY = "content";
	public static final String URL_KEY = "url";

	private int id;
	private String author;
	private String content;
	private String url;

	public MovieReviewVO(int id, String author, String content, String url) {
		this.id = id;
		this.author = author;

		// Strip out Html tags and new lines.
		this.content = android.text.Html.fromHtml(content).toString().replaceAll("/\\n|\\r/", "");

		this.url = url;
	}

	public MovieReviewVO(Parcel parcel) {
		String[] data = new String[4];

		parcel.readStringArray(data);

		this.id = Integer.valueOf(data[0]);
		this.author = data[1];
		this.content = data[2];
		this.url = data[3];
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[]{
				String.valueOf(this.id),
				this.author,
				this.content,
				this.url
		});
	}

	public static final Creator<MovieReviewVO> CREATOR = new Creator<MovieReviewVO>() {
		@Override
		public MovieReviewVO createFromParcel(Parcel parcel) {
			return new MovieReviewVO(parcel);
		}

		@Override
		public MovieReviewVO[] newArray(int size) {
			return new MovieReviewVO[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public String getUrl() {
		return url;
	}

	public static final MovieReviewVO fromJsonObject(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}

		return new MovieReviewVO(
				jsonObject.optInt(ID_KEY, 0),
				jsonObject.optString(AUTHOR_KEY, MovieVars.UNSET_VALUE),
				jsonObject.optString(CONTENT_KEY, MovieVars.UNSET_VALUE),
				jsonObject.optString(URL_KEY, MovieVars.UNSET_VALUE)
		);
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(ID_KEY, id);
		values.put(AUTHOR_KEY, author);
		values.put(CONTENT_KEY, content);
		values.put(URL_KEY, url);

		return values;
	}

}

/*
{
"id":206647,
"page":1,
"results":[
	{
		"id":"563e06159251413b1300c821",
		"author":"cutprintchris",
		"content":"<a href=\"http:\/\/www.cutprintfilm.com\/reviews\/spectre\/\"<\/a>\r\n\r\nIn hindsight my excitement for Spectre seems a bit foolish. After Skyfall, director Sam Mendes openly stated that he wouldn’t direct another Bond movie. And even so, Skyfall wasn’t the best of the new Bond films by any measure – dragging on for much too long. But somehow I got sucked into the hype of Mendes’ vision for an homage to the classic Bond, with that somewhat iconic poster of Craig mimicking Roger Moore, and trailers the emphasized a kind of retro re-visitation of some old villains and themes. But Spectre is none of those things, instead it is a film where everyone involved feels like they are just going through the motions. Spectre tries very hard to be an homage to the vintage James Bond classics, but instead ends up feeling more like a mockery of the series.\r\n\r\nFor starters the script is outrageously weak and predictable. Bond goes from shootout, to chase, to sex scene, saying and doing the exact same things he has done for the last 23 movies. Instead of a complete story, the film is just a collection of set pieces and scenes loosely stitched together. And while some of them work well on their own, by the second or third fight scene, you won’t be able to stop yourself from yawning. When you aren’t yawning you’ll be laughing, and not in a good way. The dialogue is downright cheesy. Gone is all of Bond’s smooth charm and ability to sting his opponent’s with his tongue just as much as with his gun. Instead at one point, he throws a watch bomb and says: “Time flies!”\r\n\r\nBond is one a secret mission, assigned to him by M (Judi Dench) via a video message delivered after her death. The film doesn’t ever attempt to invest the audience in this mission, or in Bond’s motivation for seeking out Oberhauser (Christoph Waltz) – which apparently has something to do with his foster-father and his childhood, again – but it never seems important to the story or to James. Meanwhile, a new joint secretary, Max or C as he is known (Andrew Scott), is attempting to unite the world’s intelligence under one surveillance network – and through doing so making the Double-0 program obsolete. Lucky for him, Sam Mendes is already doing that for him.\r\n\r\nHowever, Spectre is a beautiful film. There are about five or six huge set pieces, all of which are wonderfully filmed. And if you are just in the mood the veg out and watch Bond cruise through the streets of Rome in a prototype Aston Martin, than this is the movie for you. But things go on for entirely too long, which would be fine if something of interest were happening. But almost nothing does. Bond gets in a situation and gets out, all while throwing a few pithy, laughable, lines out.\r\n\r\nDaniel Craig has never been more disconnected from the character James Bond, than he is in Spectre. I must say first, that I love Craig as an actor and as James Bond – he is my favorite of all the Bonds. But here, he is uncharacteristically not James Bond. A scene for instance where bond throws his gun into the river, is done in such a fancy foppish way that I cringed. It is tough to properly convey how Craig misses the mark in Spectre, but when you see it, you won’t be able to help feeling the same way.\r\n\r\nMaybe Sam Mendes and screenwriter John Logan did succeed at creating a perfect homage to the Roger Moore era Bond films? Because in reality, none of those films standout as great movies. From start to finish, Spectre feels like someone filling out a madlibs of Bond scenes, and praying that when they read it back, it makes some sense.",
		"url":"https:\/\/www.themoviedb.org\/review\/563e06159251413b1300c821"
		},
		{
		"id":"563ed3f9c3a3681b4d034302",
		"author":"Frank Ochieng",
		"content":"Well, cinema’s most treasured and resilient British spy guy is back as the legendary James Bond makes his twenty-fourth outing on the big screen in the
03-13 11:15:02.249 12620-12651/net.usrlib.android.movies
 */