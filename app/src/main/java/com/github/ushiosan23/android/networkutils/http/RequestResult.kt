package com.github.ushiosan23.android.networkutils.http

/**
 * Request result interface
 */
interface RequestResult<T> {

	/**
	 * Called when response finished
	 *
	 * @param response Response object
	 */
	fun onResponse(response: HttpResponse<T>)

}
