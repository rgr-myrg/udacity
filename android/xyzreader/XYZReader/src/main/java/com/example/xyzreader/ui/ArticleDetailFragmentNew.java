package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.github.florent37.glidepalette.GlidePalette;

/**
 * Created by rgr-myrg on 5/10/16.
 */
public class ArticleDetailFragmentNew extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	public static final String NAME = ArticleDetailFragmentNew.class.getSimpleName();
	public static final String ITEM_ID_KEY = "item_id";
	public static final String NOT_AVAILABLE = "N/A";
	public static final String FONT_NAME = "Rosario-Regular.ttf";

	private Cursor mCursor;
	private View mRootView;
	private long mItemId;

	private String mCurrentPhotoUrl;

	public ArticleDetailFragmentNew() {
	}

	public static ArticleDetailFragmentNew newInstance(long itemId) {
		Bundle bundle = new Bundle();
		bundle.putLong(ITEM_ID_KEY, itemId);

		ArticleDetailFragmentNew fragment = new ArticleDetailFragmentNew();
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ITEM_ID_KEY)) {
			mItemId = getArguments().getLong(ITEM_ID_KEY);
		}

		Log.d(NAME, "onCreate mItemId: " + mItemId);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
		// the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
		// fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
		// we do this in onActivityCreated.
		getLoaderManager().initLoader(0, null, this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_article_detail_new, container, false);

		Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.fragment_article_detail_toolbar);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getParentActivity().onBackPressed();
			}
		});

		// Workaround to support disappearing image when
		// resuming from collapsed on Android JellyBean. rgr-myrg
//		((AppBarLayout) mRootView.findViewById(R.id.appbar)).addOnOffsetChangedListener(
//				new AppBarLayout.OnOffsetChangedListener() {
//					@Override
//					public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//						Log.d(NAME, String.valueOf(verticalOffset));
//						//appBarLayout.setVisibility(View.VISIBLE);
//						//(mRootView.findViewById(R.id.main_photo)).setVisibility(View.VISIBLE);
////						final int appBarHeight = appBarLayout.getHeight();
////						if (verticalOffset == -appBarHeight) {
////							onAppBarLayoutCollapsed();
////							appBarLayout.setVisibility(View.VISIBLE);
////						}
//					}
//				}
//		);

		return mRootView;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (!isAdded()) {
			if (cursor != null) {
				cursor.close();
			}

			return;
		}

		mCursor = cursor;

		if (mCursor != null && !mCursor.moveToFirst()) {
			Log.e(NAME, "Error reading item detail cursor");
			mCursor.close();
			mCursor = null;
		}

		bindArticleInfoAndDisplay();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursor = null;
		bindArticleInfoAndDisplay();
	}

	private void bindArticleInfoAndDisplay() {
		if (mRootView == null || mCursor == null) {
			mRootView.setVisibility(View.GONE);
			((TextView) mRootView.findViewById(R.id.article_title)).setText(NOT_AVAILABLE);

			return;
		}
		final String articleTitle  = mCursor.getString(ArticleLoader.Query.TITLE);
		final String articleBody   = mCursor.getString(ArticleLoader.Query.BODY);
		final String articleAuthor = mCursor.getString(ArticleLoader.Query.AUTHOR);
		final String articleDate   = DateUtils.getRelativeTimeSpanString(
				mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
				System.currentTimeMillis(),
				DateUtils.HOUR_IN_MILLIS,
				DateUtils.FORMAT_ABBREV_ALL
		).toString();

		((TextView) mRootView.findViewById(R.id.article_title)).setText(articleTitle);
		((TextView) mRootView.findViewById(R.id.article_byline)).setText(
				Html.fromHtml(articleDate
								+ " by <font color='#ffffff'>"
								+ articleAuthor
								+ "</font>"
				)
		);

		final TextView bodyTextView = (TextView) mRootView.findViewById(R.id.article_body);

		bodyTextView.setTypeface(
				Typeface.createFromAsset(
						getResources().getAssets(),
						FONT_NAME
				)
		);

		bodyTextView.setText(Html.fromHtml(articleBody));

//		loadImageWithUrl(mCursor.getString(ArticleLoader.Query.PHOTO_URL), (ImageView) mRootView.findViewById(R.id.main_photo));
//		Glide.with(getActivity())
//				.load(articlePhoto)
//				//.fitCenter()
//				.into((ImageView) mRootView.findViewById(R.id.main_photo)
//		);

//		final Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.fragment_article_detail_toolbar);
//		Glide.with(getActivity())
//				.load(articlePhoto)
//				.listener(GlidePalette.with(articlePhoto)
//								.use(GlidePalette.Profile.VIBRANT)
//								.intoBackground(toolbar)
//								.crossfade(true)
//				)
//		.into((ImageView) mRootView.findViewById(R.id.main_photo));
	//	mCurrentPhotoUrl = mCursor.getString(ArticleLoader.Query.PHOTO_URL);
		loadImageIntoView(mCursor.getString(ArticleLoader.Query.PHOTO_URL));

	}

	private void loadImageWithUrl(final String url, final ImageView imageView) {
		ImageLoaderHelper.getInstance(
				getActivity()).getImageLoader().get(
				url,
				new ImageLoader.ImageListener() {
					@Override
					public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
						Bitmap bitmap = imageContainer.getBitmap();

						if (bitmap != null) {
							//Palette p = Palette.generate(bitmap, 12);
							Palette palette = (new Palette.Builder(bitmap))
									.maximumColorCount(12)
									.generate();

							imageView.setImageBitmap(imageContainer.getBitmap());
//							mMutedColor = palette.getDarkMutedColor(0xFF333333);
//							mPhotoView.setImageBitmap(imageContainer.getBitmap());
//							mRootView.findViewById(R.id.meta_bar)
//									.setBackgroundColor(mMutedColor);
//
//							updateStatusBar();
						}
					}

					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}
				}
		);
	}

	private void loadImageIntoView(final String imageUrl) {
		Glide.with(getActivity())
				.load(imageUrl)
				.listener(
						GlidePalette.with(imageUrl)
								.use(GlidePalette.Profile.VIBRANT)
								.intoBackground(
										(Toolbar) mRootView.findViewById(
												R.id.fragment_article_detail_toolbar
										)
								)
								.crossfade(true)
				)
				.into((ImageView) mRootView.findViewById(R.id.main_photo));
	}

	private void onAppBarLayoutCollapsed() {
		Log.i(NAME, "onAppBarLayoutCollapsed is triggered.");
		//loadImageIntoView(mCurrentPhotoUrl);
	}

	private ArticleDetailActivityNew getParentActivity() {
		return (ArticleDetailActivityNew) getActivity();
	}
}
