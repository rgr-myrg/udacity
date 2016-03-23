package net.usrlib.android.movies.viewholder;

import android.view.View;
import android.widget.ImageView;

import net.usrlib.android.movies.R;

/* View Holder: Google best practices. Create a static holder for view.
 * This holder addresses performance issues for large lists.
 * See: https://youtu.be/wDBM6wVEO70?t=10m39s
 * And: http://www.piwai.info/android-adapter-good-practices/
 */

public final class ItemViewHolder {

	public final ImageView imageView;

	public ItemViewHolder(View view) {
		imageView = (ImageView) view.findViewById(R.id.movie_item_image);
	}

}
