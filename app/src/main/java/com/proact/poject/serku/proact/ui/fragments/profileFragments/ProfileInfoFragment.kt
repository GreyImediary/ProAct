package com.proact.poject.serku.proact.ui.fragments.profileFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.expel
import com.proact.poject.serku.proact.show
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile_info.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProfileInfoFragment : Fragment() {
    private val userViewModel: UserViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile_info, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userViewModel.currentUser.observe(this, Observer {
            profileEmail.text = getString(R.string.profile_email, it.email)

            setPhone(it.phone)
            setGroup(it.studentGroup)
            setDescription(it.description)
        })
    }

    private fun setPhone(phone: String?) {
        if (phone.isNullOrBlank()) {
            profilePhone.expel()
        } else {
            profilePhone.text = getString(R.string.profile_phone, phone)
            profilePhone.show()
        }
    }

    private fun setGroup(group: String?) {
        if (group.isNullOrBlank()) {
            profileGroup.expel()
        } else {
            profileGroup.text = getString(R.string.profile_group, group)
            profileGroup.show()
        }
    }

    private fun setDescription(description: String?) {
        if (description.isNullOrBlank()) {
            profileAbout.expel()
        } else {
            profileAbout.text = getString(R.string.profile_about, description)
            profileAbout.show()
        }
    }

}
