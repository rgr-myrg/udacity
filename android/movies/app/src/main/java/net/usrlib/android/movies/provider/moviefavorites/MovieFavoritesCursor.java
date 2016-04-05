package net.usrlib.android.movies.provider.moviefavorites;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import net.usrlib.android.movies.data.MoviesContract.FavoritesEntry;
import net.usrlib.android.movies.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code movie_favorites} table.
 */
public class MovieFavoritesCursor extends AbstractCursor implements MovieFavoritesModel {
    public MovieFavoritesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public Integer getId() {
        int res = getIntegerOrNull(FavoritesEntry.COLUMN_ID);
        if (res == 0)
            throw new NullPointerException("The value of '_id' in the database was zero, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code id} value.
     * Can be {@code null}.
     */
//    @Nullable
//    public Integer getId() {
//        Integer res = getIntegerOrNull(FavoritesEntry.COLUMN_ID);
//        return res;
//    }

    /**
     * Get the {@code original_title} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getOriginalTitle() {
        String res = getStringOrNull(FavoritesEntry.COLUMN_ORIGINAL_TITLE);
        if (res == null)
            throw new NullPointerException("The value of 'original_title' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPosterPath() {
        String res = getStringOrNull(FavoritesEntry.COLUMN_POSTER_PATH);
        return res;
    }

    /**
     * Get the {@code overview} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getOverview() {
        String res = getStringOrNull(FavoritesEntry.COLUMN_OVERVIEW);
        return res;
    }

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getReleaseDate() {
        String res = getStringOrNull(FavoritesEntry.COLUMN_RELEASE_DATE);
        return res;
    }

    /**
     * Get the {@code vote_count} value.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getVoteCount() {
        Integer res = getIntegerOrNull(FavoritesEntry.COLUMN_VOTE_COUNT);
        return res;
    }

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    public Double getVoteAverage() {
        Double res = getDoubleOrNull(FavoritesEntry.COLUMN_VOTE_AVERAGE);
        return res;
    }

    /**
     * Get the {@code popularity} value.
     * Can be {@code null}.
     */
    @Nullable
    public Double getPopularity() {
        Double res = getDoubleOrNull(FavoritesEntry.COLUMN_POPULARITY);
        return res;
    }
}
