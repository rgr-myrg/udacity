package net.usrlib.android.movies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.BuildConfig;
import net.usrlib.android.movies.R;
import net.usrlib.android.movies.movieapi.MovieItemVO;
import net.usrlib.android.movies.viewholder.ItemViewHolder;

import java.util.ArrayList;

public final class GridItemAdapter extends ArrayAdapter<MovieItemVO> {

	public static final String NAME = GridItemAdapter.class.getSimpleName();

	private ArrayList<MovieItemVO> mMovieItems = new ArrayList<MovieItemVO>();
	private Context mContext;

	public GridItemAdapter(final Context context, final ArrayList<MovieItemVO> arrayList) {
		super(context, 0, arrayList);

		mContext = context;
		mMovieItems = arrayList;

		if (BuildConfig.DEBUG) {
			Log.d(NAME, "GridItemAdapter context: " + context.toString());
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater
					.from(getContext())
					.inflate(R.layout.item_movie, parent, false);

			// Use a view holder to store findViewById items
			convertView.setTag(new ItemViewHolder(convertView));
		}

		ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
		MovieItemVO item = mMovieItems.get(position);

		Glide.with(mContext)
				.load(item.getImageUrl())
				.placeholder(R.drawable.image_poster_placeholder)
				.error(R.drawable.image_poster_placeholder)
				.fitCenter() //Glide doesn't scale image properly!
				.into(viewHolder.imageView);

		return convertView;
	}

	public final void updateItemsList(final ArrayList<MovieItemVO> arrayList) {
		if (BuildConfig.DEBUG) {
			Log.d(NAME, "updateItemsList size: " + String.valueOf(arrayList.size()));
		}

		mMovieItems.addAll(arrayList);
		notifyDataSetChanged();
	}

	public final ArrayList<MovieItemVO> getMovieItems() {
		return mMovieItems;
	}

}
