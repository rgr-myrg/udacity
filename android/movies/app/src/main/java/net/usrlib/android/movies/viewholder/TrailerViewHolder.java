package net.usrlib.android.movies.viewholder;

import android.view.View;
import android.widget.TextView;

import net.usrlib.android.movies.R;

public final class TrailerViewHolder {

	public final TextView titleView;

	public TrailerViewHolder(View view) {
		titleView = (TextView) view.findViewById(R.id.trailer_item_title);
	}

}
