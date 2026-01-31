package com.app.makeadonation

import android.app.Application
import android.content.Context
import com.app.makeadonation.ngocategories.di.NgoCategoryModule
import com.app.makeadonation.ngodonationreceipt.di.NgoReceiptModule
import com.app.makeadonation.ngoinstitutions.di.NgoInfoModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class MakeADonationApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MakeADonationApplication)
            modules(
                NgoCategoryModule.module,
                NgoInfoModule.module,
                NgoReceiptModule.module
            )
        }
    }

    companion object {
        private val APP_CONTEXT: Context by inject(Context::class.java)

        fun getApplicationContext(): Context {
            return APP_CONTEXT
        }
    }
}