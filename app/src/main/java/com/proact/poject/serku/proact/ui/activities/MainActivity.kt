package com.proact.poject.serku.proact.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.proact.poject.serku.proact.CURRENT_USER_EMAIL_PREF
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controller = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(controller.graph)

        mainBottomNavigation.setupWithNavController(controller)
        toolbar.setupWithNavController(controller, appBarConfiguration)

        val preferences = getPreferences(Context.MODE_PRIVATE)
        userViewModel.getUserByEmail(preferences.getString(CURRENT_USER_EMAIL_PREF, "")!!)

        fab.setOnClickListener {
            startActivity(Intent(this, AddProjectActivity::class.java))
        }

        userViewModel.currentUser.observe(this, Observer {
            if (it.userGroup != 2) {
                fab.hide()
            }
        })
    }
}
