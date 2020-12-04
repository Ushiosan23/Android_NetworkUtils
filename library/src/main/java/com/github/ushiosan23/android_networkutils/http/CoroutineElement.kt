package com.github.ushiosan23.android_networkutils.http

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Class to manage coroutine scope.
 */
internal abstract class CoroutineElement : CoroutineScope {

	/**
	 * Coroutine job element.
	 */
	@Suppress("ProtectedInFinal")
	protected val coroutineJob: Job = Job()

	/**
	 * Coroutine context property.
	 * Use default dispatcher to prevent main thread blocking.
	 */
	override val coroutineContext: CoroutineContext
		get() = coroutineJob + Dispatchers.Default

}
