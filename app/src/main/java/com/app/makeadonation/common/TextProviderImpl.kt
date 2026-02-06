package com.app.makeadonation.common

import android.content.Context

class TextProviderImpl(private val context: Context) : TextProvider {
    override fun getText(resourceString: Int): String {
        return context.getString(resourceString)
    }
}