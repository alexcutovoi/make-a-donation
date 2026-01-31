package com.app.makeadonation.common

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>() : AppCompatActivity() {
    protected lateinit var binding: VB
    protected abstract val bindingFactory: (LayoutInflater) -> VB
    protected abstract val baseToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = bindingFactory(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(baseToolbar)
        handleEdgeToEdge()
    }

    private fun handleEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )

            insets
        }
    }
}