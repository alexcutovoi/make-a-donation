package com.app.makeadonation

import android.app.Application
import android.content.Context
import com.app.makeadonation.ngocategories.di.NgoCategoriesModule
import com.app.makeadonation.ngodonationconfirmation.di.NgoDonationConfirmationModule
import com.app.makeadonation.ngoinstitutions.di.NgoInstitutionsModule
import com.app.makeadonation.ngolistdonations.di.NgoListDonationsModule
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
                NgoCategoriesModule.module,
                NgoInstitutionsModule.module,
                NgoDonationConfirmationModule.module,
                NgoListDonationsModule.module
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