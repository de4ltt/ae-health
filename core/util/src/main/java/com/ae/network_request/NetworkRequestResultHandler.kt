package com.ae.network_request

fun <T> NetworkRequestResult<T>.handleResult(): NetworkRequestResult<T> =
    if (this is NetworkRequestResult.Success)
        NetworkRequestResult.Success(this.data)
    else
        this as NetworkRequestResult.Error

fun <T, V> NetworkRequestResult<T>.handleResult(transform: (T) -> V) =
    if (this is NetworkRequestResult.Success)
        NetworkRequestResult.Success(transform(this.data))
    else
        this as NetworkRequestResult.Error