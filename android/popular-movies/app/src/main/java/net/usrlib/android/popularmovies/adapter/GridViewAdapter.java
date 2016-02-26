package net.usrlib.android.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.android.popularmovies.R;
import net.usrlib.android.popularmovies.movieapi.MovieItemVO;

import java.util.ArrayList;

/**
 * Copyright 2016 rgr-myrg
 * <p/>
 * Licensed under the Apache License:
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Created by rgr-myrg on 2/24/16 6:14 PM.
 */
public class GridViewAdapter extends ArrayAdapter<MovieItemVO> {

	private Context mContext;
	private int mLayoutResourceId;
	private ArrayList<MovieItemVO> mMovieItems = new ArrayList<MovieItemVO>();

	public GridViewAdapter(Context context, int layoutResourceId, ArrayList<MovieItemVO> movieItems) {
		super(context, layoutResourceId, movieItems);
		Log.d("GridViewAdapter", "Start up");
		mContext = context;
		mLayoutResourceId = layoutResourceId;
		mMovieItems = movieItems;
	}

	public void setGridData(ArrayList<MovieItemVO> movieItems) {
		Log.d("GridViewAdapter", "setGridData executes");
		mMovieItems = movieItems;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("GridViewAdapter", "getView executes");
		View row = convertView;
		ViewHolder holder;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
			holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		MovieItemVO item = mMovieItems.get(position);
		holder.titleTextView.setText(item.getOriginalTitle());

		//Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
		Glide.with(mContext).load(item.getImageUrl()).into(holder.imageView);
		return row;
	}

	static class ViewHolder {
		TextView titleTextView;
		ImageView imageView;
	}
}
