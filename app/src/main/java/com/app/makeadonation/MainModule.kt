package com.app.makeadonation

import com.app.makeadonation.common.TextProvider
import com.app.makeadonation.common.TextProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object MainModule {
    val module = module {
        single<TextProvider> { TextProviderImpl(androidContext()) }
    }
}