package com.ae.network_request

sealed class NetworkRequestResult<out T> {
    data class Success<out T>(val data: T) : NetworkRequestResult<T>()
    data class Error(val error: NetworkRequestError) : NetworkRequestResult<Nothing>()
}

