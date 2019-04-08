package com.proact.poject.serku.proact.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.proact.poject.serku.proact.*
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import com.proact.poject.serku.proact.viewmodels.RequestViewModel
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModel()
    private val projectViewModel: ProjectViewModel by viewModel()
    private val requestViewModel: RequestViewModel by viewModel()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val controller = findNavController(R.id.navHostFragment)

        mainBottomNavigation.setupWithNavController(controller)

        setupActionBarWithNavController(controller)

        preferences = applicationContext.getSharedPreferences(SHARED_PREF_NAME, 0)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.myProfileMenuAction -> {
                goToProfile()
                return true
            }
            R.id.logOutMenuAction -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                getSharedPreferences(SHARED_PREF_NAME, 0).edit {
                    remove(CURRENT_USER_ID_PREF)
                    remove(CURRENT_USER_EMAIL_PREF)
                }
                finish()
                return true
            }
        }

        return false
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()
    private fun goToProfile() {
        val userId = preferences.getInt(CURRENT_USER_ID_PREF, -1)
        val intent = Intent(this, ProfileActivity::class.java).apply {
            putExtra(CURRENT_USER_ID_ARG, userId)
        }
        startActivity(intent)
    }
}
