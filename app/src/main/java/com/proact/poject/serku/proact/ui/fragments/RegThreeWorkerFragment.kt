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
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.register_step_three_worker_fragment.view.*

class RegThreeWorkerFragment : Fragment() {
    private val args: RegThreeWorkerFragmentArgs by navArgs()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =
            inflater.inflate(R.layout.register_step_three_worker_fragment, container, false)

        val nameInput = layout.rgThreeNameInput
        val nameEdit = layout.rgThreeNameEdit
        val surnameInput = layout.rgThreeSurnameInput
        val surnameEdit = layout.rgThreeSurnameEdit
        val middleNameEdit = layout.rgThreeMiddlenameEdit
        val groupInput = layout.rgThreeGroupInput
        val groupEdit = layout.rgThreeGroupEdit
        val prevButton = layout.prevButton
        val nextButton = layout.nextButton


        val nameObservable = nameInput.editEmptyObservable(getString(R.string.nameinput_error))
        val surnameObservable = surnameInput.editEmptyObservable(getString(R.string.nameinput_error))
        val groupInputObservable = groupInput.editEmptyObservable(getString(R.string.groupnumberinput_error))


        disposable.add(
            Observables.combineLatest(
                nameObservable,
                surnameObservable,
                groupInputObservable
            ) { name, surname, group ->
                name && surname && group
            }.subscribe { nextButton.isEnabled = it }
        )

        nextButton.setOnClickListener {
            val role = args.role
            val email = args.email
            val pass = args.pass
            val name = nameEdit.text.toString()
            val surname = surnameEdit.text.toString()
            val middleName = middleNameEdit.text.toString()
            val group = groupEdit.text.toString()

            val action = RegThreeWorkerFragmentDirections.threeToFour(role, email, pass, name, surname, middleName, group)
            findNavController().navigate(action)
        }

        prevButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return layout
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}