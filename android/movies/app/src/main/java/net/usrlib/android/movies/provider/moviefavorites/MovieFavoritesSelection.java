package net.usrlib.android.movies.provider.moviefavorites;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import net.usrlib.android.movies.data.MoviesContract.FavoritesEntry;
import net.usrlib.android.movies.provider.base.AbstractSelection;

/**
 * Selection for the {@code movie_favorites} table.
 */
public class MovieFavoritesSelection extends AbstractSelection<MovieFavoritesSelection> {
    @Override
    protected Uri baseUri() {
        return FavoritesEntry.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MovieFavoritesCursor} object, which is positioned before the first entry, or null.
     */
    public MovieFavoritesCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MovieFavoritesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public MovieFavoritesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MovieFavoritesCursor} object, which is positioned before the first entry, or null.
     */
    public MovieFavoritesCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MovieFavoritesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public MovieFavoritesCursor query(Context context) {
        return query(context, null);
    }


    public MovieFavoritesSelection id(long... value) {
        addEquals("movie_favorites." + FavoritesEntry.COLUMN_ID, toObjectArray(value));
        return this;
    }

    public MovieFavoritesSelection idNot(long... value) {
        addNotEquals("movie_favorites." + FavoritesEntry._ID, toObjectArray(value));
        return this;
    }

//    public MovieFavoritesSelection orderById(boolean desc) {
//        orderBy("movie_favorites." + FavoritesEntry.COLUMN_ID, desc);
//        return this;
//    }

//    public MovieFavoritesSelection orderById() {
//        return orderById(false);
//    }

    public MovieFavoritesSelection id(Integer... value) {
        addEquals(FavoritesEntry.COLUMN_ID, value);
        return this;
    }

    public MovieFavoritesSelection idNot(Integer... value) {
        addNotEquals(FavoritesEntry.COLUMN_ID, value);
        return this;
    }

    public MovieFavoritesSelection idGt(int value) {
        addGreaterThan(FavoritesEntry.COLUMN_ID, value);
        return this;
    }

    public MovieFavoritesSelection idGtEq(int value) {
        addGreaterThanOrEquals(FavoritesEntry.COLUMN_ID, value);
        return this;
    }

    public MovieFavoritesSelection idLt(int value) {
        addLessThan(FavoritesEntry.COLUMN_ID, value);
        return this;
    }

    public MovieFavoritesSelection idLtEq(int value) {
        addLessThanOrEquals(FavoritesEntry.COLUMN_ID, value);
        return this;
    }

    public MovieFavoritesSelection orderById(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_ID, desc);
        return this;
    }

    public MovieFavoritesSelection orderById() {
        orderBy(FavoritesEntry.COLUMN_ID, false);
        return this;
    }

    public MovieFavoritesSelection originalTitle(String... value) {
        addEquals(FavoritesEntry.COLUMN_ORIGINAL_TITLE, value);
        return this;
    }

    public MovieFavoritesSelection originalTitleNot(String... value) {
        addNotEquals(FavoritesEntry.COLUMN_ORIGINAL_TITLE, value);
        return this;
    }

    public MovieFavoritesSelection originalTitleLike(String... value) {
        addLike(FavoritesEntry.COLUMN_ORIGINAL_TITLE, value);
        return this;
    }

    public MovieFavoritesSelection originalTitleContains(String... value) {
        addContains(FavoritesEntry.COLUMN_ORIGINAL_TITLE, value);
        return this;
    }

    public MovieFavoritesSelection originalTitleStartsWith(String... value) {
        addStartsWith(FavoritesEntry.COLUMN_ORIGINAL_TITLE, value);
        return this;
    }

    public MovieFavoritesSelection originalTitleEndsWith(String... value) {
        addEndsWith(FavoritesEntry.COLUMN_ORIGINAL_TITLE, value);
        return this;
    }

    public MovieFavoritesSelection orderByOriginalTitle(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_ORIGINAL_TITLE, desc);
        return this;
    }

    public MovieFavoritesSelection orderByOriginalTitle() {
        orderBy(FavoritesEntry.COLUMN_ORIGINAL_TITLE, false);
        return this;
    }

    public MovieFavoritesSelection posterPath(String... value) {
        addEquals(FavoritesEntry.COLUMN_POSTER_PATH, value);
        return this;
    }

    public MovieFavoritesSelection posterPathNot(String... value) {
        addNotEquals(FavoritesEntry.COLUMN_POSTER_PATH, value);
        return this;
    }

    public MovieFavoritesSelection posterPathLike(String... value) {
        addLike(FavoritesEntry.COLUMN_POSTER_PATH, value);
        return this;
    }

    public MovieFavoritesSelection posterPathContains(String... value) {
        addContains(FavoritesEntry.COLUMN_POSTER_PATH, value);
        return this;
    }

    public MovieFavoritesSelection posterPathStartsWith(String... value) {
        addStartsWith(FavoritesEntry.COLUMN_POSTER_PATH, value);
        return this;
    }

    public MovieFavoritesSelection posterPathEndsWith(String... value) {
        addEndsWith(FavoritesEntry.COLUMN_POSTER_PATH, value);
        return this;
    }

    public MovieFavoritesSelection orderByPosterPath(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_POSTER_PATH, desc);
        return this;
    }

    public MovieFavoritesSelection orderByPosterPath() {
        orderBy(FavoritesEntry.COLUMN_POSTER_PATH, false);
        return this;
    }

    public MovieFavoritesSelection overview(String... value) {
        addEquals(FavoritesEntry.COLUMN_OVERVIEW, value);
        return this;
    }

    public MovieFavoritesSelection overviewNot(String... value) {
        addNotEquals(FavoritesEntry.COLUMN_OVERVIEW, value);
        return this;
    }

    public MovieFavoritesSelection overviewLike(String... value) {
        addLike(FavoritesEntry.COLUMN_OVERVIEW, value);
        return this;
    }

    public MovieFavoritesSelection overviewContains(String... value) {
        addContains(FavoritesEntry.COLUMN_OVERVIEW, value);
        return this;
    }

    public MovieFavoritesSelection overviewStartsWith(String... value) {
        addStartsWith(FavoritesEntry.COLUMN_OVERVIEW, value);
        return this;
    }

    public MovieFavoritesSelection overviewEndsWith(String... value) {
        addEndsWith(FavoritesEntry.COLUMN_OVERVIEW, value);
        return this;
    }

    public MovieFavoritesSelection orderByOverview(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_OVERVIEW, desc);
        return this;
    }

    public MovieFavoritesSelection orderByOverview() {
        orderBy(FavoritesEntry.COLUMN_OVERVIEW, false);
        return this;
    }

    public MovieFavoritesSelection releaseDate(String... value) {
        addEquals(FavoritesEntry.COLUMN_RELEASE_DATE, value);
        return this;
    }

    public MovieFavoritesSelection releaseDateNot(String... value) {
        addNotEquals(FavoritesEntry.COLUMN_RELEASE_DATE, value);
        return this;
    }

    public MovieFavoritesSelection releaseDateLike(String... value) {
        addLike(FavoritesEntry.COLUMN_RELEASE_DATE, value);
        return this;
    }

    public MovieFavoritesSelection releaseDateContains(String... value) {
        addContains(FavoritesEntry.COLUMN_RELEASE_DATE, value);
        return this;
    }

    public MovieFavoritesSelection releaseDateStartsWith(String... value) {
        addStartsWith(FavoritesEntry.COLUMN_RELEASE_DATE, value);
        return this;
    }

    public MovieFavoritesSelection releaseDateEndsWith(String... value) {
        addEndsWith(FavoritesEntry.COLUMN_RELEASE_DATE, value);
        return this;
    }

    public MovieFavoritesSelection orderByReleaseDate(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_RELEASE_DATE, desc);
        return this;
    }

    public MovieFavoritesSelection orderByReleaseDate() {
        orderBy(FavoritesEntry.COLUMN_RELEASE_DATE, false);
        return this;
    }

    public MovieFavoritesSelection voteCount(Integer... value) {
        addEquals(FavoritesEntry.COLUMN_VOTE_COUNT, value);
        return this;
    }

    public MovieFavoritesSelection voteCountNot(Integer... value) {
        addNotEquals(FavoritesEntry.COLUMN_VOTE_COUNT, value);
        return this;
    }

    public MovieFavoritesSelection voteCountGt(int value) {
        addGreaterThan(FavoritesEntry.COLUMN_VOTE_COUNT, value);
        return this;
    }

    public MovieFavoritesSelection voteCountGtEq(int value) {
        addGreaterThanOrEquals(FavoritesEntry.COLUMN_VOTE_COUNT, value);
        return this;
    }

    public MovieFavoritesSelection voteCountLt(int value) {
        addLessThan(FavoritesEntry.COLUMN_VOTE_COUNT, value);
        return this;
    }

    public MovieFavoritesSelection voteCountLtEq(int value) {
        addLessThanOrEquals(FavoritesEntry.COLUMN_VOTE_COUNT, value);
        return this;
    }

    public MovieFavoritesSelection orderByVoteCount(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_VOTE_COUNT, desc);
        return this;
    }

    public MovieFavoritesSelection orderByVoteCount() {
        orderBy(FavoritesEntry.COLUMN_VOTE_COUNT, false);
        return this;
    }

    public MovieFavoritesSelection voteAverage(Double... value) {
        addEquals(FavoritesEntry.COLUMN_VOTE_AVERAGE, value);
        return this;
    }

    public MovieFavoritesSelection voteAverageNot(Double... value) {
        addNotEquals(FavoritesEntry.COLUMN_VOTE_AVERAGE, value);
        return this;
    }

    public MovieFavoritesSelection voteAverageGt(double value) {
        addGreaterThan(FavoritesEntry.COLUMN_VOTE_AVERAGE, value);
        return this;
    }

    public MovieFavoritesSelection voteAverageGtEq(double value) {
        addGreaterThanOrEquals(FavoritesEntry.COLUMN_VOTE_AVERAGE, value);
        return this;
    }

    public MovieFavoritesSelection voteAverageLt(double value) {
        addLessThan(FavoritesEntry.COLUMN_VOTE_AVERAGE, value);
        return this;
    }

    public MovieFavoritesSelection voteAverageLtEq(double value) {
        addLessThanOrEquals(FavoritesEntry.COLUMN_VOTE_AVERAGE, value);
        return this;
    }

    public MovieFavoritesSelection orderByVoteAverage(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_VOTE_AVERAGE, desc);
        return this;
    }

    public MovieFavoritesSelection orderByVoteAverage() {
        orderBy(FavoritesEntry.COLUMN_VOTE_AVERAGE, false);
        return this;
    }

    public MovieFavoritesSelection popularity(Double... value) {
        addEquals(FavoritesEntry.COLUMN_POPULARITY, value);
        return this;
    }

    public MovieFavoritesSelection popularityNot(Double... value) {
        addNotEquals(FavoritesEntry.COLUMN_POPULARITY, value);
        return this;
    }

    public MovieFavoritesSelection popularityGt(double value) {
        addGreaterThan(FavoritesEntry.COLUMN_POPULARITY, value);
        return this;
    }

    public MovieFavoritesSelection popularityGtEq(double value) {
        addGreaterThanOrEquals(FavoritesEntry.COLUMN_POPULARITY, value);
        return this;
    }

    public MovieFavoritesSelection popularityLt(double value) {
        addLessThan(FavoritesEntry.COLUMN_POPULARITY, value);
        return this;
    }

    public MovieFavoritesSelection popularityLtEq(double value) {
        addLessThanOrEquals(FavoritesEntry.COLUMN_POPULARITY, value);
        return this;
    }

    public MovieFavoritesSelection orderByPopularity(boolean desc) {
        orderBy(FavoritesEntry.COLUMN_POPULARITY, desc);
        return this;
    }

    public MovieFavoritesSelection orderByPopularity() {
        orderBy(FavoritesEntry.COLUMN_POPULARITY, false);
        return this;
    }
}
