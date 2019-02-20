package com.proact.poject.serku.proact.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.proact.poject.serku.proact.R
import kotlinx.android.synthetic.main.register_role_fragment.view.*

class RegRoleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.register_role_fragment, container, false)
        val workerButton = layout.workerButton
        val customerButton = layout.customerButton
        val prevButton = layout.prevButton
        val nextButton = layout.nextButton

        workerButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                workerButton.background = ContextCompat.getDrawable(activity!!, R.drawable.ic_worker_selected)
                customerButton.isChecked = false
                nextButton.isEnabled = true
            } else {
                workerButton.background = ContextCompat.getDrawable(activity!!, R.drawable.ic_worker)
            }
        }

        customerButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                customerButton.background = ContextCompat.getDrawable(activity!!, R.drawable.ic_customer_selected)
                workerButton.isChecked = false
                nextButton.isEnabled = true
            } else {
                customerButton.background = ContextCompat.getDrawable(activity!!, R.drawable.ic_customer)
            }
        }

        prevButton.setOnClickListener {
            activity?.finish()
        }

        nextButton.setOnClickListener {
            when {
                customerButton.isChecked -> nextStep(0)
                workerButton.isChecked -> nextStep(1)
            }
        }

        return layout
    }

    private fun nextStep(role: Int) {
        val action = RegRoleFragmentDirections.roleToTwoAction(role)
        findNavController().navigate(action)
    }


}