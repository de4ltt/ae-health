package com.ae.network.model

sealed class NetworkRequestError(message: String?) : Throwable() {

    data class Redirect(override val message: String? = null) : NetworkRequestError(message) {
        private fun readResolve(): Any = Redirect(message)
    }

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