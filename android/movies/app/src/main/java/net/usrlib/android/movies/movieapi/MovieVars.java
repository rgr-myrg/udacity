package net.usrlib.android.movies.movieapi;

public final class MovieVars {

	public static final String ID_TOKEN = "{id}";

	public static final String BASE_URL = "http://api.themoviedb.org/3";
	public static final String DISCOVER_MOVIE_URL = BASE_URL + "/discover/movie";

	// Change to the following new api endpoints if needed:
	//http://api.themoviedb.org/3/movie/popular?api_key=
	//http://api.themoviedb.org/3/movie/top_rated?api_key=

	public static final String TRAILERS_URL = BASE_URL + "/movie/" + ID_TOKEN + "/videos";
	public static final String REVIEWS_URL = BASE_URL + "/movie/" + ID_TOKEN + "/reviews";
	public static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185";

	public static final String API_PARAM_KEY = "api_key";
	public static final String SORT_PARAM_KEY = "sort_by";
	public static final String PAGE_PARAM_KEY = "page";
	public static final String RESULTS_KEY = "results";
	public static final String MOST_POPULAR = "popularity.desc";
	public static final String HIGHEST_RATED = "vote_count.desc";
	public static final String UNSET_VALUE = "N/A";
	public static final String PLACE_HOLDER = "http://s30.postimg.org/zfkzb977l/placeholder.png";

	public static final String VIEW_TITLE_KEY = "viewTitle";
	public static final String MOVIE_LIST_KEY = "movie_list";
	public static final String IS_DETAIL_ACTIVITY = "is_detail_activity";

	public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
	public static final String YOUTUBE_SITE = "YouTube";

	public static final String INTENT_TEXT_TYPE = "text/*";
	public static final String SHARE_TEXT = "Share This Movie!";
	public static final String SHARE_MSG = "Check out this Movie! ";

	public static final String LOADING_MSG = "Loading Movie List!";
	public static final String NO_MOVIES_MSG = "Unable to load Movie details";
	public static final String NO_REVIEWS_MSG = "No Reviews Found";
	public static final String NO_TRAILERS_MSG = "No Trailers Found";
	public static final String LIMIT_REACHED_MSG = "Request Limit Reached";

	public static final String DOTTED = "...";

	public static final int PAGE_COUNT_LIMIT = 200;
	public static final int FAVORITED_RESULT_CODE = 1;
	public static final int CONTENT_PREVIEW_LENGTH = 40;

}
