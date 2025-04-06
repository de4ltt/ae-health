package com.ae.network.model

import javax.inject.Inject

internal class SecretProperties @Inject constructor(
    override val apiBaseUri: String = "https://prodoctorov.ru/api/",
    override val mapBaseUri: String = "https://prodoctorov.ru/ajax/map/yamap_get_json/",
    override val defaultUri: String = "https://prodoctorov.ru"
) : ISecretProperties