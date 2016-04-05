package net.usrlib.android.movies.provider.moviefavorites;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.usrlib.android.movies.data.MoviesContract.FavoritesEntry;
import net.usrlib.android.movies.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code movie_favorites} table.
 */
public class MovieFavoritesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return FavoritesEntry.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MovieFavoritesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MovieFavoritesSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public MovieFavoritesContentValues putId(@Nullable Integer value) {
        mContentValues.put(FavoritesEntry.COLUMN_ID, value);
        return this;
    }

    public MovieFavoritesContentValues putIdNull() {
        mContentValues.putNull(FavoritesEntry.COLUMN_ID);
        return this;
    }

    public MovieFavoritesContentValues putOriginalTitle(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("originalTitle must not be null");
        mContentValues.put(FavoritesEntry.COLUMN_ORIGINAL_TITLE, value);
        return this;
    }


    public MovieFavoritesContentValues putPosterPath(@Nullable String value) {
        mContentValues.put(FavoritesEntry.COLUMN_POSTER_PATH, value);
        return this;
    }

    public MovieFavoritesContentValues putPosterPathNull() {
        mContentValues.putNull(FavoritesEntry.COLUMN_POSTER_PATH);
        return this;
    }

    public MovieFavoritesContentValues putOverview(@Nullable String value) {
        mContentValues.put(FavoritesEntry.COLUMN_OVERVIEW, value);
        return this;
    }

    public MovieFavoritesContentValues putOverviewNull() {
        mContentValues.putNull(FavoritesEntry.COLUMN_OVERVIEW);
        return this;
    }

    public MovieFavoritesContentValues putReleaseDate(@Nullable String value) {
        mContentValues.put(FavoritesEntry.COLUMN_RELEASE_DATE, value);
        return this;
    }

    public MovieFavoritesContentValues putReleaseDateNull() {
        mContentValues.putNull(FavoritesEntry.COLUMN_RELEASE_DATE);
        return this;
    }

    public MovieFavoritesContentValues putVoteCount(@Nullable Integer value) {
        mContentValues.put(FavoritesEntry.COLUMN_VOTE_COUNT, value);
        return this;
    }

    public MovieFavoritesContentValues putVoteCountNull() {
        mContentValues.putNull(FavoritesEntry.COLUMN_VOTE_COUNT);
        return this;
    }

    public MovieFavoritesContentValues putVoteAverage(@Nullable Double value) {
        mContentValues.put(FavoritesEntry.COLUMN_VOTE_AVERAGE, value);
        return this;
    }

    public MovieFavoritesContentValues putVoteAverageNull() {
        mContentValues.putNull(FavoritesEntry.COLUMN_VOTE_AVERAGE);
        return this;
    }

    public MovieFavoritesContentValues putPopularity(@Nullable Double value) {
        mContentValues.put(FavoritesEntry.COLUMN_POPULARITY, value);
        return this;
    }

    public MovieFavoritesContentValues putPopularityNull() {
        mContentValues.putNull(FavoritesEntry.COLUMN_POPULARITY);
        return this;
    }
}
