package com.app.makeadonation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.app.makeadonation.common.BaseActivity
import com.app.makeadonation.databinding.MainActivityBinding

class MainActivity : BaseActivity<MainActivityBinding>() {
    override val bindingFactory: (LayoutInflater) -> MainActivityBinding = MainActivityBinding::inflate
    override val baseToolbar: Toolbar by lazy {
        findViewById(R.id.main_toolbar)
    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavigation()

        /*actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }*/
    }

    private fun setupNavigation () {
        navController = (supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment).navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.ngoCategoriesFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun teste(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("INFO", "New itnent called")
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }
}