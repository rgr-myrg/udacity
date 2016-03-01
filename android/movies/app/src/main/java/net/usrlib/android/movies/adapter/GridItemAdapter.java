package net.usrlib.android.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.usrlib.android.movies.R;
import net.usrlib.android.movies.movieapi.MovieItemVO;

import java.util.ArrayList;

public class GridItemAdapter extends ArrayAdapter<MovieItemVO> {

	private ArrayList<MovieItemVO> mMovieItems = new ArrayList<MovieItemVO>();
	private Context mContext;

	public GridItemAdapter(final Context context, final ArrayList<MovieItemVO> arrayList) {
		super(context, 0, arrayList);

		mContext = context;
		mMovieItems = arrayList;
	}

/*	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item, parent, false);
		}

		MovieItemVO item = mMovieItems.get(position);

		//Commented out for now. Save for later if I want to display the title on the grid view
		//TextView textView = (TextView) convertView.findViewById(R.id.grid_item_title);
		//textView.setText(item.getOriginalTitle());

		ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
		Glide.with(mContext)
				.load(item.getImageUrl())
				.placeholder(R.drawable.image_poster_placeholder)
				.error(R.drawable.image_poster_placeholder)
				.into(imageView);

		return convertView;
	}
*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item,
					parent, false);

			imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);

			convertView.setTag(new ViewHolder(imageView));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			imageView = viewHolder.imageView;
		}

		MovieItemVO item = mMovieItems.get(position);
		Glide.with(mContext)
				.load(item.getImageUrl())
				.placeholder(R.drawable.image_poster_placeholder)
				.error(R.drawable.image_poster_placeholder)
				.into(imageView);

		return convertView;
	}

	public void updateItemsList(final ArrayList<MovieItemVO> arrayList) {
		mMovieItems.addAll(arrayList);
		notifyDataSetChanged();
	}

	public ArrayList<MovieItemVO> getMovieItems() {
		return mMovieItems;
	}

	/* ViewHolder: Google best practices. Create a static holder for data.
		 * This holder addresses performance issues for large lists.
		 * See: https://youtu.be/wDBM6wVEO70?t=10m39s
		 * And: http://www.piwai.info/android-adapter-good-practices/
		 */
	private static class ViewHolder {
		public final ImageView imageView;

		public ViewHolder(final ImageView imageView) {
			this.imageView = imageView;
		}
	}

}
