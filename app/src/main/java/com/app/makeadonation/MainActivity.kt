package com.app.makeadonation

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.app.makeadonation.common.BaseActivity
import com.app.makeadonation.databinding.MainActivityBinding

class MainActivity : BaseActivity<MainActivityBinding>() {
    override val bindingFactory: (LayoutInflater) -> MainActivityBinding = MainActivityBinding::inflate

    fun teste(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("INFO", "New itnent called")
    }
}