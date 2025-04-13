package com.ae.health

import android.app.Application
import com.ae.di.DaggerDispatchersComponent
import com.ae.di.DispatchersComponent
import com.ae.health.di.AppComponent
import com.ae.home.di.DaggerHomeComponent
import com.ae.home.di.HomeComponent
import com.ae.network.di.DaggerNetworkComponent
import com.ae.network.di.NetworkComponent
import com.ae.search.di.DaggerSearchDataComponent
import com.ae.search.di.SearchDataComponent

class AEHeathApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    lateinit var dispatchersComponent: DispatchersComponent
        private set
    lateinit var networkComponent: NetworkComponent
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

        dispatchersComponent = DaggerDispatchersComponent.create()
        networkComponent =
            DaggerNetworkComponent.builder().bindDispatchersComponent(dispatchersComponent).build()
        searchDataComponent =
            DaggerSearchDataComponent.builder().bindNetworkComponent(networkComponent).build()
        homeComponent = DaggerHomeComponent.builder().bindSearchDataComponent(searchDataComponent)
            .bindDispatchersComponent(dispatchersComponent).build()

        appComponent =
            DaggerAppComponent.builder().bindHomeComponent(homeComponent).build()

    }
}