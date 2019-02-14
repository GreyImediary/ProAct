package com.proact.poject.serku.proact.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Observer
import com.proact.poject.serku.proact.CURRENT_USER_EMAIL_PREF
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.isTextEmpty
import com.proact.poject.serku.proact.toast
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_logscreen.*
import org.koin.android.ext.android.inject

class LogScreenActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logscreen)

        val preferences = getPreferences(Context.MODE_PRIVATE)

        logscrEntryButton.setOnClickListener { verifyUser() }

        logscrPassEdit.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    verifyUser()
                    return@setOnEditorActionListener true
                }
                else -> { return@setOnEditorActionListener false }
            }
        }

        logscrRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        if (preferences.getString(CURRENT_USER_EMAIL_PREF, "") != "") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        userViewModel.userVirified.observe(this, Observer {
            if (it) {

                preferences.edit {
                    putString(CURRENT_USER_EMAIL_PREF, logscrEmailEdit.text.toString())
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            } else {
                toast(getString(R.string.logscreen_login_error))
            }
        })
    }

    private fun verifyUser() {
        if (!logscrEmailInput.isTextEmpty(getString(R.string.logscreen_emailinput_error))
            && !logscrPassInput.isTextEmpty(getString(R.string.logscreen_passinput_error))
        ) {
            val email = logscrEmailEdit.text.toString()
            val password = logscrPassEdit.text.toString()
            userViewModel.verifyUser(email, password)
        }
    }
}