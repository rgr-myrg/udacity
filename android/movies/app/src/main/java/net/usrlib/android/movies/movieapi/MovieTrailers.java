package net.usrlib.android.movies.movieapi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.usrlib.android.event.Event;
import net.usrlib.android.movies.BuildConfig;
import net.usrlib.android.movies.R;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.fragment.BaseFragment;
import net.usrlib.android.movies.parcelable.MovieTrailerVO;
import net.usrlib.android.movies.viewholder.TrailerViewHolder;
import net.usrlib.android.util.HttpRequest;
import net.usrlib.android.util.UiViewUtil;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public final class MovieTrailers {

	public static final String NAME = MovieTrailers.class.getSimpleName();

	private BaseFragment mFragment = null;
	private ViewGroup mTrailersContainer = null;

	private ArrayList<MovieTrailerVO> mMovieTrailerVOs = null;

	public MovieTrailers(BaseFragment fragment) {
		mFragment = fragment;

		MovieEvent.MovieTrailersLoaded.addListener(
				new Event.Listener() {
					@Override
					public void onComplete(Object eventData) {
						onMovieTrailersLoaded((ArrayList<MovieTrailerVO>) eventData);
					}

					@Override
					public void onError(Object eventData) {
						onMovieTrailersError();
					}
				}
		);
	}

	public final void onMovieTrailersLoaded(final ArrayList<MovieTrailerVO> trailers) {
		if (trailers == null || trailers.size() == 0 || mFragment.getActivity() == null) {
			UiViewUtil.setText(
					mFragment.getView(), R.id.movie_trailers_label, MovieVars.NO_TRAILERS_MSG
			);

			return;
		}

		mMovieTrailerVOs = trailers;

		if (BuildConfig.DEBUG) {
			Log.d(NAME, "onMovieTrailersLoaded: " + String.valueOf(mMovieTrailerVOs.size()));
		}

		if (mTrailersContainer == null) {
			mTrailersContainer = (ViewGroup) mFragment.getRootView().findViewById(R.id.movie_trailers_container);
		}

		// Set up Share Intent with the first Trailer item
		setShareButtonOnClickListener(mMovieTrailerVOs.get(0));

		final LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());

		for (final MovieTrailerVO trailerVO : mMovieTrailerVOs) {
			final View trailerView = inflater.inflate(R.layout.item_trailer, mTrailersContainer, false);
			final TrailerViewHolder trailerViewHolder = new TrailerViewHolder(trailerView);

			trailerView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse(
									trailerVO.getYoutubeUrl()
							)
					);

					mFragment.startActivity(intent);
				}
			});

			final TextView titleView = trailerViewHolder.titleView;
			titleView.setText(trailerVO.getName());

			mTrailersContainer.addView(trailerView);
		}
	}

	public final void setShareButtonOnClickListener(final MovieTrailerVO movieTrailerVO) {
		final ImageView shareBtnImageView = (ImageView) mFragment.getRootView().findViewById(R.id.button_share);

		shareBtnImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType(MovieVars.INTENT_TEXT_TYPE);
				intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

				intent.putExtra(
						Intent.EXTRA_SUBJECT,
						movieTrailerVO.getName()
				);

				intent.putExtra(
						Intent.EXTRA_TEXT,
						MovieVars.SHARE_MSG + movieTrailerVO.getYoutubeUrl()
				);

				try {
					mFragment.startActivity(Intent.createChooser(intent, MovieVars.SHARE_TEXT));
				} catch (ActivityNotFoundException e) {
					Log.e(NAME, e.getMessage());
				}
			}
		});
	}

	public final ArrayList<MovieTrailerVO> getTrailers() {
		return mMovieTrailerVOs;
	}

	public final void fetchMovieTrailersWithId(int movieId) {
		Facade.getMovieApi().fetchMovieTrailersWithId(movieId);
	}

	public final void onMovieTrailersError() {
		UiViewUtil.displayToastMessage(mFragment.getActivity(), HttpRequest.CONNECTIVY_ERROR);
		UiViewUtil.setText(mFragment.getView(), R.id.movie_trailers_label, MovieVars.NO_TRAILERS_MSG);
	}

}
