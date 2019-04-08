package com.proact.poject.serku.proact

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Observable

typealias AnyMap = Map<String, Any>

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun TextInputLayout.isTextEmpty(errorMessage: String): Boolean {
    if (editText?.text.toString().isEmpty()) {
        error = errorMessage
        return true
    } else {
        error = null
    }

    return false
}

fun TextInputLayout.editEmptyObservable(error: String) = Observable.create<Boolean> {
    this.editText?.textChanged { text ->
        if (text.isNullOrBlank()) {
            this.error = error
            it.onNext(false)
        } else {
            this.error = null
            it.onNext(true)
        }
    }
}

fun EditText.textChanged(f: (s: CharSequence?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            f(s)
        }
    })
}

fun ViewGroup.inflate(resId: Int): View =
        LayoutInflater.from(context).inflate(resId, this, false)

fun TabLayout.onSelected(
    onReselect: (tab : TabLayout.Tab?) -> Unit = {},
    onUnselect: (tab : TabLayout.Tab?) -> Unit = {},
    onSelect: (tab : TabLayout.Tab?) -> Unit = {}
) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab?) {
            onSelect(tab)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            onReselect(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            onUnselect(tab)
        }

    })
}