package com.app.makeadonation.common

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>() : AppCompatActivity() {
    protected lateinit var binding: VB
    protected abstract val bindingFactory: (LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = bindingFactory(layoutInflater)

        setContentView(binding.root)
    }
}