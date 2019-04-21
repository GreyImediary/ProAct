package com.proact.poject.serku.proact.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.proact.poject.serku.proact.*
import com.proact.poject.serku.proact.ui.activities.MainActivity
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.logscreen_fragment.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LogScreenFragment : Fragment() {

    private val userViewModel: UserViewModel by sharedViewModel()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.logscreen_fragment, container, false)

        val entryButton = layout.logscrEntryButton
        val registerButton = layout.logscrRegisterButton
        val emailInput = layout.logscrEmailInput
        val emailEdit = layout.logscrEmailEdit
        val passInput = layout.logscrPassInput
        val passEdit = layout.logscrPassEdit

        val emailObservable = emailInput.editEmptyObservable(getString(R.string.emailinput_error))
        val passObservable = passInput.editEmptyObservable(getString(R.string.passinput_error))

        val preferences = activity?.applicationContext?.getSharedPreferences(SHARED_PREF_NAME, 0)

        entryButton.setOnClickListener { verifyUser(emailEdit, passEdit) }

        if (preferences?.getString(CURRENT_USER_EMAIL_PREF, "") != "") {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        passEdit.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    verifyUser(emailEdit, passEdit)
                    return@setOnEditorActionListener true
                }
                else -> { return@setOnEditorActionListener false }
            }
        }

        registerButton.setOnClickListener {
            val action = LogScreenFragmentDirections.logToRegister()
            findNavController().navigate(action)
        }

        disposable.add(Observables.combineLatest(emailObservable, passObservable) { email, pass ->
            email && pass
        }.subscribe { entryButton.isEnabled = it })

        userViewModel.userVerified.observe(this, Observer {
            if (it != null) {
                if (it) {
                    preferences?.edit {
                        putString(CURRENT_USER_EMAIL_PREF, emailEdit.text.toString())
                    }
                    authUser(emailEdit, passEdit)

                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                } else {
                    activity?.toast(getString(R.string.logscreen_login_error))
                }
                userViewModel.userVerified.value = null
            }
        })

        userViewModel.authed.observe(this, Observer {
            preferences?.edit {
                putString(TOKEN_PREF, it)
            }
        })

        return layout
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun verifyUser(emailEdit: EditText, passEdit: EditText) {
            val email = emailEdit.text.toString()
            val password = passEdit.text.toString()
            userViewModel.verifyUser(email, password)
        }

    private fun authUser(emailEdit: EditText, passEdit: EditText) {
        val email = emailEdit.text.toString()
        val password = passEdit.text.toString()
        userViewModel.authUser(email, password)
    }
}