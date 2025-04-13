package com.ae.config

import dagger.Component

@ConfigScope
@Component
interface SecretPropertiesComponent {

    fun

    @Component.Builder
    interface Builder {
        fun build(): SecretPropertiesComponent
    }
}