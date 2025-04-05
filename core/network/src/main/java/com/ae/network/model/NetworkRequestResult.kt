package com.ae.network.model

sealed class NetworkRequestResult<out T> {
    data class Success<out T>(val data: T) : NetworkRequestResult<T>()
    data class Error(val error: NetworkRequestError) : NetworkRequestResult<Nothing>()
}

sealed class NetworkRequestError : Throwable() {
    data object ClientError : NetworkRequestError() {
        private fun readResolve(): Any = ClientError
    }

    data object ServerError : NetworkRequestError() {
        private fun readResolve(): Any = ServerError
    }

    data object UnknownError : NetworkRequestError() {
        private fun readResolve(): Any = UnknownError
    }
}