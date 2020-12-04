package com.github.ushiosan23.android_networkutils.functions;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public interface IListener<T> {

	/**
	 * Called when a response is received.
	 */
	void onResponse(T response);

	/**
	 * Callback method that an error has been occurred with the provided error code and optional
	 * user-readable message.
	 */
	default void onErrorResponse(VolleyError error) {
	}

}
