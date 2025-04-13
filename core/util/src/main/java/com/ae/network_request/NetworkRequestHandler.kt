package com.ae.network_request

import retrofit2.Response

internal suspend fun <T> handleNetworkRequest(
    request: suspend () -> Response<T>
): NetworkRequestResult<T> = try {
    val response = request()

    if (response.body() == null)
        throw Exception("Body is null")

    if (response.isSuccessful)
        NetworkRequestResult.Success(response.body()!!)
    else NetworkRequestResult.Error(
        error = when (response.code()) {
            in 400..499 -> NetworkRequestError.ClientError(response.code().toString())
            in 500..599 -> NetworkRequestError.ServerError(response.code().toString())
            else -> NetworkRequestError.UnknownError(response.code().toString())
        }
    )
} catch (e: Throwable) {
    NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
}