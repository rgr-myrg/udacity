package net.usrlib.android.movies.movieapi;

public class MovieVars {

	public static final String BASE_URL = "http://api.themoviedb.org/3";
	public static final String DISCOVER_MOVIE_URL = BASE_URL + "/discover/movie";
	public static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185";
	public static final String ID_TOKEN = "{id}";
	public static final String VIDEOS_URL = BASE_URL + "/movie/" + ID_TOKEN + "/videos";
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
	public static final int PAGE_COUNT_LIMIT = 200;

}
