package com.app.makeadonation.framework.storage.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.app.makeadonation.framework.storage.Shared
import com.app.makeadonation.framework.storage.SharedConstants
import com.app.makeadonation.framework.storage.SharedImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object StorageModule {
    val module = module {
        single<SharedPreferences> {
            androidContext().getSharedPreferences(SharedConstants.NAME, MODE_PRIVATE)
        }

        factoryOf(::SharedImpl) bind Shared::class
    }
}