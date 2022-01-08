package com.example.cleanlayout.business.data.network

import javax.inject.Singleton

object NetworkErrors {

    const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
    const val UNABLE_TODO_INTERNET_OPERATION = "Cant do that operation without connection"
    const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection"
    const val NETWORK_ERROR_UNKNOWN = "Unkown network error"
    const val NETWORK_ERROR = "Network error"
    const val NETWORK_ERROR_TIMEOUT = "Network timeout"
    const val NETWORK_DATA_NULL = "Network data is null"

    fun isNetworkError(msg: String) =
        when {
            msg.contains(UNABLE_TO_RESOLVE_HOST) -> true
            else -> false
        }
}
