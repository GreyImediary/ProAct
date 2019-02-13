package com.proact.poject.serku.proact

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

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