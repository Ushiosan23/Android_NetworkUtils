package com.github.ushiosan23.android.networkutils.http

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject

/**
 * Volley http response
 *
 * @param T Target generic type
 */
class HttpResponse<T> {

	/**
	 * Response status code
	 */
	var statusCode: Int = 0
		internal set

	/**
	 * Response request headers
	 */
	var responseHeaders: Map<String, String> = emptyMap()
		internal set

	/**
	 * Response body content
	 */
	var responseBody: T? = null
		internal set

	/**
	 * Response error
	 */
	var responseError: Throwable? = null
		internal set

	/**
	 * Check if is valid json response
	 */
	var isJsonResponse: Boolean = when (responseBody) {
		is JsonObject, is JSONObject -> true
		is String -> isJsonString(responseBody as String)
		else -> false
	}

	/**
	 * Convert response to json object
	 *
	 * @return [JsonObject] decoded result or `null` if response data is not valid json
	 */
	fun convertToJson(): JsonObject? = when (responseBody) {
		is JsonObject -> responseBody as JsonObject
		is JSONObject -> stringToJsonObject((responseBody as JSONObject).toString())
		is String -> stringToJsonObject(responseBody as String)
		else -> null
	}

	/**
	 * Companion object
	 */
	companion object {

		/**
		 * Check if string is valid json
		 *
		 * @return [Boolean] result
		 */
		private fun isJsonString(data: String): Boolean = try {
			Json.decodeFromString<JsonObject>(data)
			true
		} catch (err: Exception) {
			false
		}

		/**
		 * Decode string to Json object
		 *
		 * @param data Target data to decode
		 *
		 * @return [JsonObject] decoded result or `null` if data is not valid
		 */
		private fun stringToJsonObject(data: String): JsonObject? = if (isJsonString(data))
			Json.decodeFromString<JsonObject>(data)
		else
			null

	}
}
