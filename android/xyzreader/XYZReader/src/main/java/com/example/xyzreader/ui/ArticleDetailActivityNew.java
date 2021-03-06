package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;

public class ArticleDetailActivityNew extends AppCompatActivity
		implements LoaderManager.LoaderCallbacks<Cursor> {

	private ViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;
	private Cursor mCursor;
	private long mSelectedItemId;
	private long mStartId;

	private int mItemPosition = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_article_detail_new);

		getLoaderManager().initLoader(0, null, this);
		initPagerView();

//		if (savedInstanceState == null) {
//			if (getIntent() != null && getIntent().getData() != null) {
//				mStartId = ItemsContract.Items.getItemId(getIntent().getData());
//				mSelectedItemId = mStartId;
//			}
//		}
		if (savedInstanceState == null) {
			final Intent intent = getIntent();

			if (intent != null && intent.getData() != null) {
				mStartId = ItemsContract.Items.getItemId(intent.getData());
				mSelectedItemId = mStartId;
				mItemPosition = intent.getIntExtra(ArticleListActivity.NAME, 0);
			}
		}

		Log.d("DETAIL", "mItemId: " + mItemPosition);
	}

	public static void start(Context c) {
		c.startActivity(new Intent(c, ArticleDetailActivityNew.class));
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return ArticleLoader.newAllArticlesInstance(this);
	}

//	@Override
//	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//		mCursor = data;
//		mPagerAdapter.notifyDataSetChanged();
//
//		// Select the start ID
//		if (mStartId > 0) {
//			mCursor.moveToFirst();
//			// TODO: optimize
//			while (!mCursor.isAfterLast()) {
//				if (mCursor.getLong(ArticleLoader.Query._ID) == mStartId) {
//					final int position = mCursor.getPosition();
//					mViewPager.setCurrentItem(position, false);
//					break;
//				}
//
//				mCursor.moveToNext();
//			}
//
//			mStartId = 0;
//		}
//	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
		if (data == null) {
			return;
		}

		mCursor = data;
		mPagerAdapter.notifyDataSetChanged();

		mCursor.moveToPosition(mItemPosition);
		mViewPager.setCurrentItem(mItemPosition, false);

		mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursor = null;
		mPagerAdapter.notifyDataSetChanged();
	}

	// Floating Action Button onClick Handler
	public void onFABClicked(View view) {
		startActivity(
				Intent.createChooser(
						ShareCompat.IntentBuilder.from(this)
								.setType("text/plain")
								.setText(getString(R.string.action_share_text))
								.getIntent(), getString(R.string.action_share)
				)
		);
	}

	private void initPagerView() {
		mPagerAdapter = new MyPagerAdapter(getFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.detail_view_pager);
		mViewPager.setAdapter(mPagerAdapter);
//		mViewPager.setPageMargin((int) TypedValue.applyDimension(
//						TypedValue.COMPLEX_UNIT_DIP,
//						1,
//						getResources().getDisplayMetrics()
//				)
//		);
//
//		mViewPager.setPageMarginDrawable(new ColorDrawable(0x22000000));

		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (mCursor != null) {
					mCursor.moveToPosition(position);
				}

				mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
			}
		});
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			mCursor.moveToPosition(position);
			return ArticleDetailFragmentNew.newInstance(mCursor.getLong(ArticleLoader.Query._ID));
		}

		@Override
		public int getCount() {
			return (mCursor != null) ? mCursor.getCount() : 0;
		}
	}
}
