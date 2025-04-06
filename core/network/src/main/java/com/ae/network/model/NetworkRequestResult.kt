package com.ae.network.model

sealed class NetworkRequestResult<out T> {
    data class Success<out T>(val data: T) : NetworkRequestResult<T>()
    data class Error(val error: NetworkRequestError) : NetworkRequestResult<Nothing>()
}

sealed class NetworkRequestError(message: String?) : Throwable() {
    data class ClientError(override val message: String? = null) : NetworkRequestError(message) {
        private fun readResolve(): Any = ClientError(message)
    }

    data class ServerError(override val message: String? = null) : NetworkRequestError(message) {
        private fun readResolve(): Any = ServerError(message)
    }

    data class UnknownError(override val message: String? = null) : NetworkRequestError(message) {
        private fun readResolve(): Any = UnknownError(message)
    }
}