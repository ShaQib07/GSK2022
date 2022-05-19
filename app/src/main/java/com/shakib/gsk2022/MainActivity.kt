package com.shakib.gsk2022

import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.shakib.gsk2022.common.base.BaseActivity
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var selectedImages: ArrayList<Image> = ArrayList()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun configureViews(savedInstanceState: Bundle?) {
        super.configureViews(savedInstanceState)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}