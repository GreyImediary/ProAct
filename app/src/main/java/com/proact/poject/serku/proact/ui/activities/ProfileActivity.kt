package com.proact.poject.serku.proact.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.proact.poject.serku.proact.CURRENT_USER_ID_ARG
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModel()
    private val projectViewModel: ProjectViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userId = intent.getIntExtra(CURRENT_USER_ID_ARG, -1)!!

        profileBottomNavigation.setupWithNavController(findNavController(R.id.navHostFragment))

        userViewModel.getUserById(userId)
        projectViewModel.getActiveUserProjects(userId)
        projectViewModel.getFinishedUserProjects(userId)

        userViewModel.currentUser.observe(this, Observer {
            profileFullName.text = "${it.name} ${it.surname} ${it.middleName}"

            if (!it.avatarUrl.isNullOrBlank()) {
                Picasso.get()
                    .load(it.avatarUrl)
                    .into(profileAvatar)
            }
        })
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()
}