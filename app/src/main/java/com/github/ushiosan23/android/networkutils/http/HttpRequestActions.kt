package com.github.ushiosan23.android.networkutils.http

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

/**
 * Http object actions
 */
object HttpRequestActions : CoroutineScope {


	/* ---------------------------------------------------------
	 *
	 * Internal properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Coroutine job executor
	 */
	private val coroutineJob: Job = Job()

	/* ---------------------------------------------------------
	 *
	 * Implemented properties
	 *
	 * --------------------------------------------------------- */

	/**
	 * Coroutine context property.
	 *
	 * This coroutine is not launched in main thread
	 */
	override val coroutineContext: CoroutineContext
		get() = coroutineJob + Dispatchers.Default

	/* ---------------------------------------------------------
	 *
	 * Callback methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Make request by callback interface
	 *
	 * @param url Target request url
	 * @param method Target http method
	 * @param data Target http data request
	 * @param headers Target custom headers to send
	 * @param result Interface callback
	 */
	@JvmStatic
	fun requestURI(
		url: String,
		method: HttpMethod = HttpMethod.GET,
		data: Map<String, String>? = null,
		headers: Map<String, String>? = null,
		result: RequestResult<String>
	) = HttpConnector.requireClient {
		// Make request object
		val responseObj = HttpResponse<String>()
		val request = createRequest(url, method, responseObj, result, data, headers)
		// Add request to queue
		HttpConnector.getClient().add(request)
	}

	/* ---------------------------------------------------------
	 *
	 * Asynchronous methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Make request by coroutines
	 *
	 * @param url Target request url
	 * @param method Target http method
	 * @param data Target http data request
	 * @param headers Target custom headers to send
	 */
	@JvmStatic
	suspend fun requestURIAsync(
		url: String,
		method: HttpMethod = HttpMethod.GET,
		data: Map<String, String>? = null,
		headers: Map<String, String>? = null
	): HttpResponse<String> = suspendCancellableCoroutine { coroutine ->
		// Require initialize client
		HttpConnector.requireClient {
			// Create request
			val response = HttpResponse<String>()
			val request = createRequestAsync(url, method, response, coroutine, data, headers)
			// Add request to queue
			HttpConnector.getClient().add(request)
		}
	}

	/* ---------------------------------------------------------
	 *
	 * Internal methods
	 *
	 * --------------------------------------------------------- */

	/**
	 * Get method type
	 *
	 * @param method Target local method
	 *
	 * @return [Int] volley method id
	 * @see Request.Method
	 */
	private fun getMethodByType(method: HttpMethod): Int = when (method) {
		HttpMethod.GET -> Request.Method.GET
		HttpMethod.POST -> Request.Method.POST
		HttpMethod.PUT -> Request.Method.PUT
		HttpMethod.PATCH -> Request.Method.PATCH
		HttpMethod.DELETE -> Request.Method.DELETE
	}

	/**
	 * Create request by interface callback
	 *
	 * @param url Target url to make request
	 * @param method Target http method
	 * @param httpResponse Object response
	 * @param requestResult Interface result
	 * @param data Request data
	 * @param headers Request headers
	 *
	 * @return [StringRequest] Request result object
	 */
	private fun createRequest(
		url: String,
		method: HttpMethod,
		httpResponse: HttpResponse<String>,
		requestResult: RequestResult<String>,
		data: Map<String, String>? = null,
		headers: Map<String, String>? = null
	): StringRequest = object : StringRequest(
		getMethodByType(method),
		url,
		{ greatResult(it, httpResponse, requestResult) },
		{ badResult(it, httpResponse, requestResult) }
	) {

		private val requestData: MutableMap<String, String> =
			data?.toMutableMap() ?: mutableMapOf()

		private val requestHeaders: MutableMap<String, String> =
			headers?.toMutableMap() ?: mutableMapOf()

		override fun getParams(): MutableMap<String, String>? = if (method.acceptData)
			requestData
		else
			null

		override fun getHeaders(): MutableMap<String, String> = if (method.acceptData) {
			requestHeaders["Content-Type"] = "application/x-www-form-urlencoded"
			requestHeaders
		} else {
			requestHeaders
		}

		override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
			httpResponse.statusCode = response.statusCode
			httpResponse.responseHeaders = response.headers
			return super.parseNetworkResponse(response)
		}

	}

	/**
	 * Create asynchronous request
	 *
	 * @param url Target url to make request
	 * @param method Target http method
	 * @param httpResponse Object response
	 * @param coroutine Target coroutine object
	 * @param data Request data
	 * @param headers Request headers
	 *
	 * @return [StringRequest] Request result object
	 */
	private fun createRequestAsync(
		url: String,
		method: HttpMethod,
		httpResponse: HttpResponse<String>,
		coroutine: CancellableContinuation<HttpResponse<String>>,
		data: Map<String, String>? = null,
		headers: Map<String, String>? = null
	): StringRequest = object : StringRequest(
		getMethodByType(method),
		url,
		{ greatResult(it, httpResponse, coroutine) },
		{ badResult(it, httpResponse, coroutine) }
	) {

		private val requestData: MutableMap<String, String> =
			data?.toMutableMap() ?: mutableMapOf()

		private val requestHeaders: MutableMap<String, String> =
			headers?.toMutableMap() ?: mutableMapOf()

		override fun getParams(): MutableMap<String, String>? = if (method.acceptData)
			requestData
		else
			null

		override fun getHeaders(): MutableMap<String, String> = if (method.acceptData) {
			requestHeaders["Content-Type"] = "application/x-www-form-urlencoded"
			requestHeaders
		} else {
			requestHeaders
		}

		override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
			httpResponse.statusCode = response.statusCode
			httpResponse.responseHeaders = response.headers
			return super.parseNetworkResponse(response)
		}

	}

	/**
	 * Great request result by interface
	 *
	 * @param result Request result
	 * @param response Target response object
	 * @param requestResult Target interface callback
	 */
	private fun greatResult(
		result: String,
		response: HttpResponse<String>,
		requestResult: RequestResult<String>
	) {
		response.responseBody = result
		requestResult.onResponse(response)
	}

	/**
	 * Great request result by interface
	 *
	 * @param result Request result
	 * @param response Target response object
	 * @param coroutine Target coroutine object
	 */
	private fun greatResult(
		result: String,
		response: HttpResponse<String>,
		coroutine: CancellableContinuation<HttpResponse<String>>
	) {
		response.responseBody = result
		coroutine.resume(response)
	}

	/**
	 * Bad request result by interface
	 *
	 * @param result Request result
	 * @param response Target response object
	 * @param requestResult Target interface callback
	 */
	private fun badResult(
		result: VolleyError,
		response: HttpResponse<String>,
		requestResult: RequestResult<String>
	) {
		result.networkResponse?.let {
			response.responseBody = it.data.toString(Charsets.UTF_8)
			response.statusCode = it.statusCode
			response.responseHeaders = it.headers
		}
		response.responseError = result
		requestResult.onResponse(response)
	}

	/**
	 * Bad request result by interface
	 *
	 * @param result Request result
	 * @param response Target response object
	 * @param coroutine Target coroutine object
	 */
	private fun badResult(
		result: VolleyError,
		response: HttpResponse<String>,
		coroutine: CancellableContinuation<HttpResponse<String>>
	) {
		result.networkResponse?.let {
			response.responseBody = it.data.toString(Charsets.UTF_8)
			response.statusCode = it.statusCode
			response.responseHeaders = it.headers
		}
		response.responseError = result
		coroutine.resume(response)
	}

}
