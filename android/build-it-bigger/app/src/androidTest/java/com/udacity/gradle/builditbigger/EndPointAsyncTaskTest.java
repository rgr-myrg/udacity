package com.udacity.gradle.builditbigger;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;

import com.udacity.gradle.builditbigger.task.EndpointAsyncTask;

import net.usrlib.pattern.TinyEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by rgr-myrg on 5/3/16.
 */
@RunWith(AndroidJUnit4.class)
public class EndPointAsyncTaskTest extends ActivityTestCase {
	private boolean mHasOnSuccess;
	private String mJokeContent = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testTaskExecute() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);

		EndpointAsyncTask.OnPostExecute.addListener(new TinyEvent.Listener(){
			@Override
			public void onSuccess(Object data) {
				mJokeContent = (String) data;
				mHasOnSuccess = true;
				signal.countDown();
			}

			@Override
			public void onError(Object data) {
				mHasOnSuccess = false;
				fail();
				signal.countDown();
			}
		});

		new EndpointAsyncTask().execute();

		signal.await(30, TimeUnit.SECONDS);

		assertTrue("OnPostExecute should have been invoked.", mHasOnSuccess);
		assertNotNull("OnPostExecute should return content.", mJokeContent);
	}
}
