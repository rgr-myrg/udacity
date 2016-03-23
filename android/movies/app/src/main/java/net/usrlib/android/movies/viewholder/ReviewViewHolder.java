package net.usrlib.android.movies.viewholder;

import android.view.View;
import android.widget.TextView;

import net.usrlib.android.movies.R;

public final class ReviewViewHolder {

	public final TextView circleView;
	public final TextView authorView;
	public final TextView contentView;

	public ReviewViewHolder(View view) {
		circleView = (TextView) view.findViewById(R.id.review_circle_icon);
		authorView = (TextView) view.findViewById(R.id.review_item_author);
		contentView = (TextView) view.findViewById(R.id.review_item_content);
	}

}
