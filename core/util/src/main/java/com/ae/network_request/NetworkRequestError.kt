package com.ae.network_request

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