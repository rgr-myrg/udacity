/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine.wearable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class WeatherWatchFace extends CanvasWatchFaceService {
	public static final String NAME = WeatherWatchFace.class.getSimpleName();
	public static final String MAP_REQUEST_PATH = "/sunshine_map_request_path";

	private static final Typeface NORMAL_TYPEFACE = Typeface.create(
			Typeface.SANS_SERIF, Typeface.NORMAL
	);

	/**
	 * Update rate in milliseconds for interactive mode. We update once a second since seconds are
	 * displayed in interactive mode.
	 */
	private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

	/**
	 * Handler message id for updating the time periodically in interactive mode.
	 */
	public static final int MSG_UPDATE_TIME = 0;

	@Override
	public Engine onCreateEngine() {
		return new Engine();
	}

	public class Engine extends CanvasWatchFaceService.Engine implements
			GoogleApiClient.ConnectionCallbacks,
			GoogleApiClient.OnConnectionFailedListener,
			DataApi.DataListener {
		private final Handler mUpdateTimeHandler = new EngineHandler(this);
		boolean mRegisteredTimeZoneReceiver = false;

		private Paint mBackgroundPaint;
		private Paint mTextPaint;

		private Bitmap mIconBitmap;

		private Paint mCurrentTimePaint;
		private Paint mDatePaint;
		private Paint mIconPaint;
		private Paint mMaxTempPaint;
		private Paint mMinTempPaint;

		private String[] mShortDayNames;
		private String[] mShortMonthNames;

		private String mMaxTempValue;
		private String mMinTempValue;

		private GoogleApiClient mGoogleApiClient;

		private boolean mAmbient;
		private Time mTime;
		private int mTapCount;
		private float mXOffset;
		private float mYOffset;

		/**
		 * Whether the display supports fewer bits for each color in ambient mode. When true, we
		 * disable anti-aliasing in ambient mode.
		 */
		private boolean mLowBitAmbient;

		final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mTime.clear(intent.getStringExtra("time-zone"));
				mTime.setToNow();
				invalidate();
			}
		};

		@Override
		public void onCreate(SurfaceHolder holder) {
			super.onCreate(holder);
			Log.d(NAME, "onCreate");

			setWatchFaceStyle(new WatchFaceStyle.Builder(WeatherWatchFace.this)
					.setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
					.setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
					.setShowSystemUiTime(false)
					.setAcceptsTapEvents(true)
					.build());

			mTime = new Time();

			final DateFormatSymbols symbols = new DateFormatSymbols();

			mShortDayNames = symbols.getShortWeekdays();
			mShortMonthNames = symbols.getShortMonths();

			createPaintItems();

			mGoogleApiClient = new GoogleApiClient.Builder(WeatherWatchFace.this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(Wearable.API)
					.build();
			//mGoogleApiClient.connect();
		}

		@Override
		public void onDestroy() {
			mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
			super.onDestroy();
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);

			if (visible) {
				registerReceiver();

				mGoogleApiClient.connect();
				// Update time zone in case it changed while we weren't visible.
				mTime.clear(TimeZone.getDefault().getID());
				mTime.setToNow();
			} else {
				unregisterReceiver();

				Wearable.DataApi.removeListener(mGoogleApiClient, Engine.this);

				if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
					mGoogleApiClient.disconnect();
				}
			}

			// Whether the timer should be running depends on whether we're visible (as well as
			// whether we're in ambient mode), so we may need to start or stop the timer.
			updateTimer();
		}

		@Override
		public void onConnected(Bundle bundle) {
			Log.d(NAME, "onConnected");
			Wearable.DataApi.addListener(mGoogleApiClient, Engine.this);
		}

		@Override
		public void onConnectionSuspended(int i) {

		}

		@Override
		public void onDataChanged(DataEventBuffer dataEventBuffer) {
			Log.d(NAME, "onDataChanged");
			if (dataEventBuffer == null) {
				Log.w(NAME, "onDataChanged dataEventBuffer is null.");
				return;
			}

			for (DataEvent dataEvent : dataEventBuffer) {
				if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
					DataItem dataItem = dataEvent.getDataItem();
					Log.d(NAME, "onDataChanged: " + dataItem.getUri().getPath());
					if (dataItem.getUri().getPath().compareTo(BuildConfig.MAP_REQUEST_PATH) == 0) {
						DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();

						mMaxTempValue = dataMap.getString(BuildConfig.MAP_MAX_TEMP_KEY);
						mMinTempValue = dataMap.getString(BuildConfig.MAP_MIN_TEMP_KEY);

						runWeatherBitmapTask(dataMap.getAsset(BuildConfig.MAP_ICON_KEY));
					}
				}
			}
		}

		private void runWeatherBitmapTask(final Asset asset) {
			if (asset == null || mGoogleApiClient == null) {
				return;
			}

			new AsyncTask<Asset, Void, Void>() {
				@Override
				protected Void doInBackground(Asset... params) {
					final ConnectionResult result = mGoogleApiClient.blockingConnect(
							500, TimeUnit.MILLISECONDS
					);

					if(!result.isSuccess()) {
						return null;
					}

					final InputStream inputStream = Wearable.DataApi
							.getFdForAsset(mGoogleApiClient, params[0])
							.await()
							.getInputStream();

					if (inputStream == null) {
						return null;
					}

					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					if (bitmap == null) {
						return null;
					}

					// Need a more precise representation of the size. Convert float to double then
					// retrieve the intValue. Good post on SO:
					// http://stackoverflow.com/questions/916081/convert-float-to-double-without-losing-precision
					final int watchFaceIconSize = Double.valueOf(
							WeatherWatchFace.this
									.getResources()
									.getDimension(R.dimen.digital_icon_size)
					).intValue();

					mIconBitmap = Bitmap.createScaledBitmap(
							bitmap,
							watchFaceIconSize,
							watchFaceIconSize,
							false
					);

					// Send an empty message to the Handler
					postInvalidate();
					return null;
				}
			}.execute(asset);
		}

		@Override
		public void onConnectionFailed(ConnectionResult connectionResult) {

		}

		@Override
		public void onApplyWindowInsets(WindowInsets insets) {
			super.onApplyWindowInsets(insets);

			// Load resources that have alternate values for round watches.
			final Resources resources = WeatherWatchFace.this.getResources();
			final boolean isRound = insets.isRound();

			mXOffset = resources.getDimension(isRound
					? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);

//			float textSize = resources.getDimension(isRound
//					? R.dimen.digital_text_size_round : R.dimen.digital_text_size);

			float tempTextSize = resources.getDimension(isRound
					? R.dimen.digital_temp_text_size_round : R.dimen.digital_temp_text_size);

			mMaxTempPaint.setTextSize(tempTextSize);
			mMinTempPaint.setTextSize(tempTextSize);

			mCurrentTimePaint.setTextSize(
					resources.getDimension(isRound
									? R.dimen.digital_time_text_size_round
									: R.dimen.digital_time_text_size
					)
			);
			mDatePaint.setTextSize(resources.getDimension(isRound
							? R.dimen.digital_date_text_size_round
							: R.dimen.digital_date_text_size)
			);

//			mTextPaint.setTextSize(textSize);
		}

		@Override
		public void onPropertiesChanged(Bundle properties) {
			super.onPropertiesChanged(properties);
			mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
		}

		@Override
		public void onTimeTick() {
			super.onTimeTick();
			invalidate();
		}

		@Override
		public void onAmbientModeChanged(boolean inAmbientMode) {
			super.onAmbientModeChanged(inAmbientMode);
			if (mAmbient != inAmbientMode) {
				mAmbient = inAmbientMode;
				if (mLowBitAmbient) {
					//mTextPaint.setAntiAlias(!inAmbientMode);
					mMaxTempPaint.setAntiAlias(!inAmbientMode);
					mMinTempPaint.setAntiAlias(!inAmbientMode);
				}
				invalidate();
			}

			// Whether the timer should be running depends on whether we're visible (as well as
			// whether we're in ambient mode), so we may need to start or stop the timer.
			updateTimer();
		}

		/**
		 * Captures tap event (and tap type) and toggles the background color if the user finishes
		 * a tap.
		 */
		@Override
		public void onTapCommand(int tapType, int x, int y, long eventTime) {
			Resources resources = WeatherWatchFace.this.getResources();
			switch (tapType) {
				case TAP_TYPE_TOUCH:
					// The user has started touching the screen.
					break;
				case TAP_TYPE_TOUCH_CANCEL:
					// The user has started a different gesture or otherwise cancelled the tap.
					break;
				case TAP_TYPE_TAP:
					// The user has completed the tap gesture.
					mTapCount++;
					mBackgroundPaint.setColor(resources.getColor(mTapCount % 2 == 0 ?
							R.color.background : R.color.background2));
					break;
			}
			invalidate();
		}

		@Override
		public void onDraw(Canvas canvas, Rect bounds) {
			// Draw the background.
			if (isInAmbientMode()) {
				canvas.drawColor(Color.BLACK);
			} else {
				canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);
			}

			// Draw H:MM in ambient mode or H:MM:SS in interactive mode.
			mTime.setToNow();

//			String text = mAmbient
//					? String.format("%d:%02d", mTime.hour, mTime.minute)
//					: String.format("%d:%02d:%02d", mTime.hour, mTime.minute, mTime.second);
//			canvas.drawText(text, mXOffset, mYOffset, mTextPaint);

			if (!mAmbient) {
				drawDateTime(canvas, bounds);
			}
		}

		private void drawDateTime(final Canvas canvas, final Rect bounds) {
			final float centerXValue = bounds.centerX();
			final String hourMinute  = String.format("%02d:%02d", mTime.hour, mTime.minute);

			float xOffsetValue = centerXValue - mCurrentTimePaint.measureText(hourMinute) / 2;
			float yOffsetValue = mYOffset + getResources().getDimension(R.dimen.digital_time_text_margin_bottom);

			canvas.drawText(
					hourMinute,
					xOffsetValue,
					mYOffset,
					mCurrentTimePaint
			);

			final String currentDate = String.format(
					"%s, %s %d %d",
					mShortDayNames[mTime.weekDay],
					mShortMonthNames[mTime.month],
					mTime.monthDay,
					mTime.year
			);

			xOffsetValue = centerXValue - mDatePaint.measureText(currentDate) / 2;

			canvas.drawText(
					currentDate.toUpperCase(),
					xOffsetValue,
					yOffsetValue,
					mDatePaint
			);

			if (!mLowBitAmbient) {
				drawWeatherInfo(canvas, centerXValue, yOffsetValue);
			}
		}

		private void drawWeatherInfo(final Canvas canvas, final float x, final float y) {
			Log.d("WATCHFACE", "drawWeatherInfo");
			Log.d("WATCHFACE", "mMaxTempValue: " + mMaxTempValue);

			if (mIconBitmap == null || mIconPaint == null) {
				return;
			}

			float yOffsetValue = y + getResources().getDimension(R.dimen.digital_date_text_margin_bottom);
			float leftPosition = x - mIconBitmap.getWidth() - mIconBitmap.getWidth() / 4;
			float topPosition = yOffsetValue - mIconBitmap.getHeight() / 2;

			canvas.drawBitmap(
					mIconBitmap,
					leftPosition,
					topPosition,
					mIconPaint
			);
			canvas.drawText(
					mMaxTempValue,
					x,
					yOffsetValue,
					mMaxTempPaint
			);

			float xOffsetValue = x + mMaxTempPaint.measureText(mMaxTempValue)
					+ getResources().getDimension(R.dimen.digital_temp_text_margin_right);

			canvas.drawText(
					mMinTempValue,
					xOffsetValue,
					yOffsetValue,
					mMinTempPaint
			);
		}

		private void registerReceiver() {
			if (mRegisteredTimeZoneReceiver) {
				return;
			}
			mRegisteredTimeZoneReceiver = true;
			IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
			WeatherWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
		}

		private void unregisterReceiver() {
			if (!mRegisteredTimeZoneReceiver) {
				return;
			}
			mRegisteredTimeZoneReceiver = false;
			WeatherWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
		}

		/**
		 * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
		 * or stops it if it shouldn't be running but currently is.
		 */
		private void updateTimer() {
			mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
			if (shouldTimerBeRunning()) {
				mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
			}
		}

		/**
		 * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
		 * only run when we're visible and in interactive mode.
		 */
		private boolean shouldTimerBeRunning() {
			return isVisible() && !isInAmbientMode();
		}

		/**
		 * Handle updating the time periodically in interactive mode.
		 */
		private void handleUpdateTimeMessage() {
			invalidate();
			if (shouldTimerBeRunning()) {
				long timeMs = System.currentTimeMillis();
				long delayMs = INTERACTIVE_UPDATE_RATE_MS
						- (timeMs % INTERACTIVE_UPDATE_RATE_MS);
				mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
			}
		}

		private void createPaintItems() {
			final Resources resources = WeatherWatchFace.this.getResources();

			mYOffset = resources.getDimension(R.dimen.digital_y_offset);

			mBackgroundPaint = new Paint();
			mBackgroundPaint.setColor(resources.getColor(R.color.background_blue));

			mTextPaint = createPaintWithResource(resources, R.color.digital_text_white);

			mCurrentTimePaint = createPaintWithResource(resources, R.color.digital_text_white);
			mDatePaint = createPaintWithResource(resources, R.color.digital_text_blue);
			mMaxTempPaint = createPaintWithResource(resources, R.color.digital_text_white);
			mMinTempPaint = createPaintWithResource(resources, R.color.digital_text_blue);

			mIconPaint = new Paint();
		}

		private Paint createPaintWithResource(final Resources resources, final int colorId) {
			final Paint paint = new Paint();
			paint.setColor(resources.getColor(colorId));
			paint.setTypeface(NORMAL_TYPEFACE);
			paint.setAntiAlias(true);

			return paint;
		}
	}

	private static class EngineHandler extends Handler {
		private final WeakReference<WeatherWatchFace.Engine> mWeakReference;

		public EngineHandler(WeatherWatchFace.Engine reference) {
			mWeakReference = new WeakReference<>(reference);
		}

		@Override
		public void handleMessage(Message msg) {
			WeatherWatchFace.Engine engine = mWeakReference.get();
			if (engine == null) {
				return;
			}

			switch (msg.what) {
				case WeatherWatchFace.MSG_UPDATE_TIME:
					engine.handleUpdateTimeMessage();
					break;
			}
		}
	}
}
