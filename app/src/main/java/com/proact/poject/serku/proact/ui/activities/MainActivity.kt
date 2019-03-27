package com.proact.poject.serku.proact.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.proact.poject.serku.proact.*
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

        val preferences = applicationContext.getSharedPreferences(SHARED_PREF_NAME, 0)

        fab.setOnClickListener {
            startActivity(Intent(this, AddProjectActivity::class.java))
        }

        userViewModel.currentUser.observe(this, Observer {
            if (it.userGroup != 2) {
                fab.hide()
            }
            preferences.edit {
                putInt(CURRENT_USER_ID_PREF, it.id)
                putInt(CURRENT_USER_USER_GROUP_PREF, it.userGroup)
            }
        })

        userViewModel.getUserByEmail(preferences.getString(CURRENT_USER_EMAIL_PREF, "")!!)
    }
}
