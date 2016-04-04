package net.usrlib.android.movies.movieapi;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.R;
import net.usrlib.android.movies.facade.Facade;
import net.usrlib.android.movies.fragment.BaseFragment;
import net.usrlib.android.movies.parcelable.MovieItemVO;
import net.usrlib.android.movies.viewholder.ResourceHolder;
import net.usrlib.android.util.UiViewUtil;
import net.usrlib.pattern.TinyEvent;

public class MovieDetails {

	public static final String NAME = MovieDetails.class.getSimpleName();

	private BaseFragment mFragment = null;

	public MovieDetails(BaseFragment fragment) {
		mFragment = fragment;

		MovieEvent.MovieSetAsFavorite.addListener(new TinyEvent.Listener() {
			@Override
			public void onSuccess(Object eventData) {
				UiViewUtil.displayToastMessage(
						mFragment.getActivity(), ResourceHolder.getSavedFavoriteMsg()
				);
			}
		});

		MovieEvent.MovieUnsetAsFavorite.addListener(new TinyEvent.Listener() {
			@Override
			public void onSuccess(Object eventData) {
				UiViewUtil.displayToastMessage(
						mFragment.getActivity(), ResourceHolder.getRemovedFavoriteMsg()
				);
			}
		});
	}

	public final void loadMovieDetail(final MovieItemVO movieItemVO) {
		if (movieItemVO == null || mFragment.getActivity() == null) {
			UiViewUtil.setText(
					mFragment.getRootView(),
					R.id.movie_title,
					MovieVars.NO_MOVIES_MSG
			);

			return;
		}

		final View rootView = mFragment.getRootView();
		final ImageView posterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
		final ImageView favBtnImageView = (ImageView) rootView.findViewById(R.id.button_favorite);

		if (Facade.getIsTablet()) {
			UiViewUtil.applyNextColorOnTextView(rootView, R.id.movie_title);

//					((TextView) rootView.findViewById(R.id.movie_title))
//					.getBackground().setColorFilter(
//					Color.parseColor(ColorUtil.getNextColorBrightTheme()),
//					PorterDuff.Mode.SRC
//			);
		}

		// Invoking placeholder causes the image to become misaligned. >:(
		Glide.with(mFragment.getActivity())
				.load(movieItemVO.getImageUrl())
						//.placeholder(R.drawable.image_poster_placeholder)
						//.error(R.drawable.image_poster_placeholder)
						//.crossFade()
				.fitCenter()
				.into(posterImageView);

		int resourceId = R.drawable.heart_unselected;

		if (Facade.getMoviesDBHelper().isMovieSetAsFavorite(movieItemVO.getId())) {
			resourceId = R.drawable.heart_selected;
		}

		favBtnImageView.setImageResource(resourceId);

		// Set Tag to help toggle favorites icon on/off in setMovieIfLiked()
		favBtnImageView.setTag(resourceId);

		favBtnImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMovieIfLiked(movieItemVO, (ImageView) v);
			}
		});

		UiViewUtil.setText(
				rootView,
				R.id.movie_title,
				movieItemVO.getOriginalTitle()
		);

		UiViewUtil.setText(
				rootView,
				R.id.movie_release_date,
				movieItemVO.getReleaseDate()
		);

		UiViewUtil.setText(
				rootView,
				R.id.movie_rating,
				String.valueOf(movieItemVO.getVoteAverage())
		);

		UiViewUtil.setText(
				rootView,
				R.id.movie_overview,
				movieItemVO.getOverview()
		);
	}

	private void setMovieIfLiked(final MovieItemVO movieItemVO, final ImageView imageView) {
		final boolean hasSelected = (Integer) imageView.getTag() != R.drawable.heart_selected;

		final int imageResource = hasSelected
				? R.drawable.heart_selected
				: R.drawable.heart_unselected;

		Facade.getMoviesDBHelper().setMovieAsFavorite(movieItemVO.toContentValues(), hasSelected);

		imageView.setImageResource(imageResource);
		imageView.setTag(imageResource);

		Intent intent = new Intent();
		intent.putExtra(MovieVars.IS_DETAIL_ACTIVITY, true);

		// Set Result Code and Intent for onActivityResult()
		mFragment.getActivity().setResult(MovieVars.FAVORITED_RESULT_CODE, intent);
	}

}
