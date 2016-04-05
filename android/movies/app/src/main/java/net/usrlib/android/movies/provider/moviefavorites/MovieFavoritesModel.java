package net.usrlib.android.movies.provider.moviefavorites;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.usrlib.android.movies.provider.base.BaseModel;

/**
 * Favorite Movies
 */
public interface MovieFavoritesModel extends BaseModel {

    /**
     * Get the {@code id} value.
     * Can be {@code null}.
     */
    @Nullable
    Integer getId();

    /**
     * Get the {@code original_title} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getOriginalTitle();

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterPath();

    /**
     * Get the {@code overview} value.
     * Can be {@code null}.
     */
    @Nullable
    String getOverview();

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    String getReleaseDate();

    /**
     * Get the {@code vote_count} value.
     * Can be {@code null}.
     */
    @Nullable
    Integer getVoteCount();

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    Double getVoteAverage();

    /**
     * Get the {@code popularity} value.
     * Can be {@code null}.
     */
    @Nullable
    Double getPopularity();
}
