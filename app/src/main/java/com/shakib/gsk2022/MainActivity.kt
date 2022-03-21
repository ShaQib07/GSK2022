package com.shakib.gsk2022

import com.shakib.gsk2022.common.base.BaseActivity
import com.shakib.gsk2022.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

}