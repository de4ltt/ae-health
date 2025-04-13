package com.ae.health

import android.app.Application
import com.ae.config.di.ConfigComponent
import com.ae.config.di.DaggerConfigComponent
import com.ae.di.DaggerDispatchersComponent
import com.ae.di.DispatchersComponent
import com.ae.health.config.ConfigProperties
import com.ae.health.di.AppComponent
import com.ae.health.di.DaggerAppComponent
import com.ae.home.di.DaggerHomeComponent
import com.ae.home.di.HomeComponent
import com.ae.network.di.CoreNetworkComponent
import com.ae.network.di.DaggerCoreNetworkComponent
import com.ae.search.di.DaggerSearchDataComponent
import com.ae.search.di.SearchDataComponent
import com.ae.search.network.di.DaggerNetworkSearchComponent
import com.ae.search.network.di.NetworkSearchComponent

class AEHeathApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    lateinit var coreNetworkComponent: CoreNetworkComponent
        private set
    lateinit var configComponent: ConfigComponent
        private set
    lateinit var utilComponent: DispatchersComponent
        private set

    lateinit var networkSearchComponent: NetworkSearchComponent
        private set

    lateinit var searchDataComponent: SearchDataComponent
        private set

    lateinit var homeComponent: HomeComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initializeComponents()
        appComponent.inject(this)
    }

    private fun initializeComponents() {

        configComponent = DaggerConfigComponent.builder()
            .bindSecretProperties(ConfigProperties)
            .build()
        utilComponent =
            DaggerDispatchersComponent.create()
        coreNetworkComponent =
            DaggerCoreNetworkComponent.builder()
                .bindConfigComponent(configComponent)
                .build()

        networkSearchComponent =
            DaggerNetworkSearchComponent.builder()
                .bindCoreNetworkComponent(coreNetworkComponent)
                .bindDispatchersComponent(utilComponent)
                .bindConfigComponent(configComponent)
                .build()

        searchDataComponent =
            DaggerSearchDataComponent.builder()
                .bindNetworkSearchComponent(networkSearchComponent)
                .build()

        homeComponent = DaggerHomeComponent.builder()
            .bindSearchDataComponent(searchDataComponent)
            .bindUtilComponent(utilComponent)
            .build()

        appComponent =
            DaggerAppComponent.builder()
                .bindHomeComponent(homeComponent)
                .build()

    }
}