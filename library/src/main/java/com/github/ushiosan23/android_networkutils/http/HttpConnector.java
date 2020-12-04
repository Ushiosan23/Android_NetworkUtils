package com.github.ushiosan23.android_networkutils.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.ushiosan23.android_networkutils.exceptions.UnInitializePropertyException;
import com.github.ushiosan23.android_networkutils.functions.IRequire0;
import com.github.ushiosan23.android_networkutils.functions.IRequire1;

/**
 * Create a chanel to make http request.
 */
public final class HttpConnector {

	/* ---------------------------------------------------------
	 *
	 * Properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Volley request queue.
	 */
	private static RequestQueue queue;

	/* ---------------------------------------------------------
	 *
	 * Static methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Initialize http connector.
	 * I recommend to initialize this class in Application class.
	 *
	 * @param context Application context.
	 */
	public static void initialize(Context context) {
		queue = Volley.newRequestQueue(context);
	}

	/**
	 * Get current request queue.
	 *
	 * @return {@link RequestQueue} Current request instance.
	 */
	static RequestQueue getQueue() {
		return requireQueue(() -> queue);
	}

	/**
	 * Check if current request queue was initialized.
	 *
	 * @param action Action to launch.
	 * @param <T>    Object to return.
	 * @return {@link T} instance.
	 */
	public static <T> T requireQueue(IRequire1<T> action) {
		if (queue == null)
			throw new UnInitializePropertyException("Connector is not initialized.");

		return action.launch();
	}

	/**
	 * Check if current request queue was initialized.
	 *
	 * @param action Action to launch.
	 */
	public static void requireQueue(IRequire0 action) {
		if (queue == null)
			throw new UnInitializePropertyException("Connector is not initialized.");

		action.launch();
	}

}
