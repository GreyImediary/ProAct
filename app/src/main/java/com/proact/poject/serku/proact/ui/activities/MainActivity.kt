package com.proact.poject.serku.proact.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.proact.poject.serku.proact.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controller = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(controller.graph)

        mainBottomNavigation.setupWithNavController(controller)
        toolbar.setupWithNavController(controller, appBarConfiguration)
    }
}
