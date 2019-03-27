package com.proact.poject.serku.proact.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.proact.poject.serku.proact.CURRENT_USER_EMAIL_PREF
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.SHARED_PREF_NAME
import com.proact.poject.serku.proact.toast
import com.proact.poject.serku.proact.ui.activities.MainActivity
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.register_step_four_fragment.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class RegFourFragment : Fragment() {
    private val args: RegFourFragmentArgs by navArgs()
    private val userViewModel: UserViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.register_step_four_fragment, container, false)

        val role = args.role
        val name = args.name
        val surname = args.surname
        val middleName = args.middlename
        val email = args.email
        val pass = args.pass
        val groupNumber = args.groupNumber

        layout.doneButton.setOnClickListener {
            val phone = layout.rgFourPhoneEdit.text.toString()
            val about = layout.rgFourAboutEdit.text.toString()
            userViewModel.addUser(name, surname, middleName, email, pass, phone, groupNumber, about, role)
        }

        layout.prevButton.setOnClickListener { findNavController().navigateUp() }

        userViewModel.userAdded.observe(this, Observer {
            if (it) {
                activity?.application?.getSharedPreferences(SHARED_PREF_NAME, 0)?.edit {
                    putString(CURRENT_USER_EMAIL_PREF, email)
                }
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            } else {
                activity?.toast(getString(R.string.register_error))
            }
        })

        return  layout
    }
}