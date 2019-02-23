package com.proact.poject.serku.proact.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.editEmptyObservable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.register_step_three_customer_fragment.view.*

class RegThreeCustomerFragment : Fragment() {
    private val args: RegThreeCustomerFragmentArgs by navArgs()
    private val disposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.register_step_three_customer_fragment, container, false)

        val nameEdit = layout.rgThreeNameEdit
        val surnameInput = layout.rgThreeSurnameInput
        val surnameEdit = layout.rgThreeSurnameEdit
        val middleNameEdit = layout.rgThreeMiddlenameEdit
        val prevButton = layout.prevButton
        val nextButton = layout.nextButton

        val surnameObservable = surnameInput.editEmptyObservable(getString(R.string.surnameinput_customer_error))
        disposable.add(surnameObservable.subscribe { nextButton.isEnabled = it })

        nextButton.setOnClickListener {
            val role = args.role
            val email = args.email
            val pass = args.pass
            val name = nameEdit.text.toString()
            val surname = surnameEdit.text.toString()
            val middleName = middleNameEdit.text.toString()

            val action = RegThreeCustomerFragmentDirections.threeToFour(role, email, pass, name, surname, middleName)
            findNavController().navigate(action)
        }

        prevButton.setOnClickListener { findNavController().navigateUp() }

        return layout
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}