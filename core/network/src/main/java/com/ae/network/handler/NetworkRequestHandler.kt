package com.ae.network.handler

import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import retrofit2.Response

suspend fun <T> handleNetworkRequest(
    request: suspend () -> Response<T>
): NetworkRequestResult<T> = try {
    val response = request()

    if (response.body() == null)
        throw Exception("Body is null")

    if (response.isSuccessful)
        NetworkRequestResult.Success(response.body()!!)
    else NetworkRequestResult.Error(
        error = when (response.code()) {
            in 300..399 -> NetworkRequestError.Redirect(response.code().toString())
            in 400..499 -> NetworkRequestError.ClientError(response.code().toString())
            in 500..599 -> NetworkRequestError.ServerError(response.code().toString())
            else -> NetworkRequestError.UnknownError(response.code().toString())
        }
    )
} catch (e: Throwable) {
    NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
}