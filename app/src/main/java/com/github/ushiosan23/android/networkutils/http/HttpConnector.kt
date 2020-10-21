package com.github.ushiosan23.android.networkutils.http

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Http connector object.
 *
 * This object is used to manage basic http methods
 */
object HttpConnector {

    /* ---------------------------------------------------------
     *
     * Internal properties
     *
     * --------------------------------------------------------- */

    /**
     * Create volley client
     */
    private var volleyClient: RequestQueue? = null

    /* ---------------------------------------------------------
     *
     * Public methods
     *
     * --------------------------------------------------------- */

    /**
     * Initialize connector
     *
     * This method is recommended to call on application creation.
     *
     * @param context Application context
     */
    fun initializeConnector(context: Context) {
        if (volleyClient != null) return
        volleyClient = Volley.newRequestQueue(context)
    }

    /* ---------------------------------------------------------
     *
     * Internal methods
     *
     * --------------------------------------------------------- */

    /**
     * Get instance client
     *
     * @return [RequestQueue] volley client
     */
    internal fun getClient(): RequestQueue = requireClientReturn {
        return@requireClientReturn volleyClient!!
    }

    /**
     * Require client and launch only if is initialized
     *
     * @param block Body invocation
     *
     * @throws UninitializedPropertyAccessException Error if client is not initialized
     */
    internal fun requireClient(block: () -> Unit) {
        if (volleyClient == null)
            errorInitializeClient()
        block.invoke()
    }

    /**
     * Require client and launch only if is initialized
     *
     * @param block Body invocation
     * @param T Type generic
     *
     * @return [T] generic type
     * @throws UninitializedPropertyAccessException Error if client is not initialized
     */
    internal fun <T> requireClientReturn(block: () -> T): T {
        if (volleyClient == null)
            errorInitializeClient()
        return block()
    }

    /**
     * Error if client is not initialized
     *
     * @throws UninitializedPropertyAccessException Error if client is not initialized
     */
    private fun errorInitializeClient() {
        throw UninitializedPropertyAccessException("${this::class.java.simpleName} has not been initialized.")
    }

}
