package com.proact.poject.serku.proact.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.textChanged
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.register_step_two_fragment.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class RegTwoFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModel()
    private val args: RegTwoFragmentArgs by navArgs()
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.register_step_two_fragment, container, false)

        val emailEdit = layout.rgTwoEmailEdit
        val emailInput = layout.rgTwoEmailInput
        val passInput = layout.rgTwoPassInput
        val passEdit = layout.rgTwoPassEdit
        val nextButton = layout.nextButton
        val prevButton = layout.prevButton

        val emailCheckSubject = PublishSubject.create<Boolean>()
        val passCheckSubject = PublishSubject.create<Boolean>()

        val emailRegEx = """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}${'$'}""".toRegex()


        emailEdit.textChanged { text ->
            if (text.isNullOrBlank()) {
                emailCheckSubject.onNext(false)
                emailInput.error = getString(R.string.emailinput_error)
            } else {
                emailInput.error = null
                emailCheckSubject.onNext(true)
            }
        }

        disposable.add(emailRegExObservable(emailEdit)
            .debounce(400, TimeUnit.MILLISECONDS)
            .filter {
                it.length >= 5
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!emailRegEx.matches(it)) {
                    emailInput.error = getString(R.string.email_regex_error)
                    emailCheckSubject.onNext(false)
                } else {
                    emailInput.error = null
                }
            })

        passEdit.textChanged { text ->
            if (text.isNullOrBlank()) {
                passCheckSubject.onNext(false)
                passInput.error = getString(R.string.passinput_error)
            } else {
                passInput.error = null
                passCheckSubject.onNext(true)
            }
        }

        nextButton.setOnClickListener { userViewModel.isUserRegistered(emailEdit.text.toString()) }

        prevButton.setOnClickListener { findNavController().navigateUp() }


        userViewModel.isRegistered.observe(this, Observer {
            if (it != null) {
                if (it) {
                    emailInput.error = getString(R.string.email_exist)
                } else {
                    val role = args.role
                    val email = emailEdit.text.toString()
                    val pass = passEdit.text.toString()
                    val action = RegTwoFragmentDirections.twoToThree(role, email, pass)
                    findNavController().navigate(action)
                    userViewModel.isRegistered.value = null
                }
            }
        })

        disposable.add(Observables.combineLatest(emailCheckSubject, passCheckSubject) { email, pass ->
            email && pass
        }.subscribe { nextButton.isEnabled = it })

        return layout
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun emailRegExObservable(editText: EditText) = Observable.create<String> {
        editText.textChanged { text ->
            it.onNext(text.toString())
        }
    }

}