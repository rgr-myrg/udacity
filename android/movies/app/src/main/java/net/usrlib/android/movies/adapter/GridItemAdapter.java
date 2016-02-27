package net.usrlib.android.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.R;
import net.usrlib.android.movies.movieapi.MovieItemVO;

import java.util.ArrayList;

public class GridItemAdapter extends ArrayAdapter<MovieItemVO> {

	private ArrayList<MovieItemVO> mMovieItems = new ArrayList<MovieItemVO>();
	private Context mContext;

	public GridItemAdapter(Context context, ArrayList<MovieItemVO> arrayList) {
		super(context, 0, arrayList);

		mContext = context;
		mMovieItems = arrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item, parent, false);
		}

		MovieItemVO item = mMovieItems.get(position);

		TextView textView = (TextView) convertView.findViewById(R.id.grid_item_title);
		textView.setText(item.getOriginalTitle());

		ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
		Glide.with(mContext).load(item.getImageUrl()).into(imageView);

		return convertView;
	}

	public void updateItemsList(ArrayList<MovieItemVO> arrayList) {
		mMovieItems.addAll(arrayList);
		//mMovieItems = arrayList;
		notifyDataSetChanged();
	}
}
