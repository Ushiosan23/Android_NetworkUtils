package com.github.ushiosan23.android_networkutils.http;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.ushiosan23.android_networkutils.functions.IListener;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Map;

/**
 * Class used to make http request.
 */
public class HttpRequestAction extends CoroutineElement {

	/* ---------------------------------------------------------
	 *
	 * Internal properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Request url
	 */
	private URI requestURI;

	/**
	 * Request headers
	 */
	private Map<String, String> requestHeaders;

	/* ---------------------------------------------------------
	 *
	 * Constructors
	 *
	 * --------------------------------------------------------- */

	/**
	 * Create action request with url.
	 *
	 * @param uri Target url to make request.
	 */
	public HttpRequestAction(URI uri) {
		requestURI = uri;
	}

	/**
	 * Create action request with uri.
	 *
	 * @param uri Target uri to make request
	 */
	public HttpRequestAction(String uri) {
		this(URI.create(uri));
	}

	/* ---------------------------------------------------------
	 *
	 * Public methods
	 *
	 * --------------------------------------------------------- */

	public void existsAsync(IListener<Boolean> listener) {


	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */



	/* ---------------------------------------------------------
	 *
	 * Internal classes
	 *
	 * --------------------------------------------------------- */

	/**
	 * Class to create request.
	 */
	public static class SimpleRequest extends StringRequest {

		/**
		 * Create request status code.
		 */
		private int statusCode = -1;

		/**
		 * Create request by  method url listener and error listener.
		 *
		 * @param method        Target request method
		 * @param url           Target url to make request
		 * @param listener      Result listener
		 * @param errorListener Error listener
		 */
		public SimpleRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
			super(method, url, listener, errorListener);
		}

		/**
		 * Create request by url listener and error listener.
		 *
		 * @param url           Target url to make request
		 * @param listener      Result listener
		 * @param errorListener Error listener
		 */
		public SimpleRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
			super(url, listener, errorListener);
		}

		/**
		 * Parse response network result.
		 *
		 * @param response Target network response.
		 * @return {@link Response} target response.
		 */
		@Override
		protected Response<String> parseNetworkResponse(@NotNull NetworkResponse response) {
			statusCode = response.statusCode;
			return super.parseNetworkResponse(response);
		}

	}

}
