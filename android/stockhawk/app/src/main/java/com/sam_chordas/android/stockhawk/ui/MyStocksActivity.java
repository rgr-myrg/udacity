package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.melnykov.fab.FloatingActionButton;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.api.StockEvent;
import com.sam_chordas.android.stockhawk.constants.StockVars;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.rest.RecyclerViewItemClickListener;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.service.StockIntentService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.touch_helper.SimpleItemTouchHelperCallback;
import com.sam_chordas.android.stockhawk.util.NetworkUtil;
import com.sam_chordas.android.stockhawk.util.UiUtil;

import net.usrlib.pattern.TinyEvent;

public class MyStocksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private Intent mServiceIntent;
	private ItemTouchHelper mItemTouchHelper;
	private QuoteCursorAdapter mCursorAdapter;
	private Context mContext;
	private Cursor mCursor;

	private TinyEvent.Listener mListener = new TinyEvent.Listener() {
		@Override
		public void onError(Object data) {
			Log.d("MAIN", "onError");
			onQuoteNotFound();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UiUtil.onCreate(this);
		StockEvent.QuoteLoaded.addListener(mListener);

		mContext = this;

		setContentView(R.layout.activity_stocks);

		// The intent service is for executing immediate pulls from the Yahoo API
		// GCMTaskService can only schedule tasks, they cannot execute immediately
		mServiceIntent = new Intent(this, StockIntentService.class);

		if (savedInstanceState == null) {
			// Run the initialize task service so that some stocks appear upon an empty database

			startServiceWithTag(StockVars.TAG_INIT);
		}

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		getLoaderManager().initLoader(StockVars.CURSOR_LOADER_ID, null, this);

		mCursorAdapter = new QuoteCursorAdapter(this, null);

		recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
				new RecyclerViewItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View v, int position) {
						mCursor.moveToPosition(position);

						Intent chartIntent = new Intent(mContext, ChartActivity.class);
						chartIntent.putExtra(
								QuoteColumns.SYMBOL,
								mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL))
						);

						startActivity(chartIntent);
					}
				}));

		recyclerView.setAdapter(mCursorAdapter);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.attachToRecyclerView(recyclerView);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isNetworkAvailable()) {
					displayNetworkNotAvailableDialog();
					return;
				}

				new MaterialDialog.Builder(mContext).title(R.string.symbol_search)
						.content(R.string.content_test)
						.inputType(InputType.TYPE_CLASS_TEXT)
						.input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
							@Override
							public void onInput(MaterialDialog dialog, CharSequence input) {
								// On FAB click, receive user input. Make sure the stock doesn't already exist
								// in the DB and proceed accordingly
								Cursor c = getContentResolver().query(
										QuoteProvider.Quotes.CONTENT_URI,
										new String[]{QuoteColumns.SYMBOL},
										QuoteColumns.SYMBOL + "= ?",
										new String[]{input.toString()},
										null
								);

								if (c.getCount() != 0) {
									UiUtil.displayStockExistsMsg(MyStocksActivity.this);

									return;
								} else {
									// Add the stock to DB
									mServiceIntent.putExtra(StockVars.TAG_KEY, StockVars.TAG_ADD);
									mServiceIntent.putExtra(QuoteColumns.SYMBOL, input.toString());
									startService(mServiceIntent);
								}
							}
						})
						.show();

			}
		});

		ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
		mItemTouchHelper = new ItemTouchHelper(callback);
		mItemTouchHelper.attachToRecyclerView(recyclerView);

		mTitle = getTitle();
		if (isNetworkAvailable()) {
			long period = 3600L;
			long flex = 10L;

			// create a periodic task to pull stocks once every hour after the app has been opened. This
			// is so Widget data stays up to date.
			PeriodicTask periodicTask = new PeriodicTask.Builder()
					.setService(StockTaskService.class)
					.setPeriod(period)
					.setFlex(flex)
					.setTag(StockVars.TAG_PERIODIC)
					.setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
					.setRequiresCharging(false)
					.build();
			// Schedule task with tag "periodic." This ensure that only the stocks present in the DB
			// are updated.
			GcmNetworkManager.getInstance(this).schedule(periodicTask);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(StockVars.CURSOR_LOADER_ID, null, this);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_stocks, menu);
		restoreActionBar();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
			case R.id.action_change_units:
				onActionChangeUnits();
				break;
			case R.id.action_refresh:
				onRefresh();
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This narrows the return to only the stocks that are most current.
		return new CursorLoader(
				this, QuoteProvider.Quotes.CONTENT_URI,
				new String[]{
						QuoteColumns._ID,
						QuoteColumns.SYMBOL,
						QuoteColumns.BIDPRICE,
						QuoteColumns.PERCENT_CHANGE,
						QuoteColumns.CHANGE,
						QuoteColumns.ISUP
				},
				QuoteColumns.ISCURRENT + " = ?",
				new String[]{"1"},
				null
		);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mCursorAdapter.swapCursor(data);
		mCursor = data;

		if (data.getCount() <= 0) {
			// Toggle Empty Stock Visibility?
			Log.d("MAIN", "onLoadFinished no data!!!");
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);
	}

	private void startServiceWithTag(final String tagValue) {
		if (mServiceIntent == null) {
			return;
		}

		mServiceIntent.putExtra(StockVars.TAG_KEY, tagValue);

		if (isNetworkAvailable()) {
			startService(mServiceIntent);
		} else {
			displayNetworkNotAvailableDialog();
		}
	}

	private void onActionChangeUnits() {
		Utils.showPercent = !Utils.showPercent;
		this.getContentResolver().notifyChange(QuoteProvider.Quotes.CONTENT_URI, null);
	}

	private void onRefresh() {
		UiUtil.displayRefreshingMsg(this);
		startServiceWithTag(StockVars.TAG_PERIODIC);
	}

	private boolean isNetworkAvailable() {
		return NetworkUtil.isNetworkAvailable(mContext);
	}

	private void displayNetworkNotAvailableDialog() {
		UiUtil.showNetworkNotAvailableDialog(mContext);
	}

	private void onQuoteNotFound() {
		UiUtil.displayStockNotFoundMsg(this);
	}
}
