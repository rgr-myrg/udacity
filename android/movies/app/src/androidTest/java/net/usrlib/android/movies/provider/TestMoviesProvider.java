package net.usrlib.android.movies.provider;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.test.AndroidTestCase;

import net.usrlib.android.movies.data.MoviesContract;

public class TestMoviesProvider extends AndroidTestCase {

	public void testProviderRegistry() {
		PackageManager pm = mContext.getPackageManager();

		// We define the component name based on the package name from the context and the
		// WeatherProvider class.
		ComponentName componentName = new ComponentName(mContext.getPackageName(),
				MoviesProvider.class.getName());
		try {
			// Fetch the provider info using the component name from the PackageManager
			// This throws an exception if the provider isn't registered.
			ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

			// Make sure that the registered authority matches the authority from the Contract.
			assertEquals("Error: MoviesProvider registered with authority: " + providerInfo.authority +
							" instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
					providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);
		} catch (PackageManager.NameNotFoundException e) {
			// I guess the provider isn't registered correctly.
			assertTrue("Error: MoviesProvider not registered at " + mContext.getPackageName(),
					false);
		}
	}

//	public void testGetType() {
//		String contentType = mContext.getContentResolver().getType(MoviesContract.FavoritesEntry.CONTENT_URI);
//
//		assertNotNull("");
//		// Should be "vnd.android.cursor.dir/net.usrlib.android.movies._data.data/favorites"
//		assertEquals(
//				"MoviesEntry.CONTENT_URI should return FavoritesEntry.CONTENT_TYPE",
//				MoviesContract.FavoritesEntry.CONTENT_TYPE,
//				contentType
//		);
//	}
}
