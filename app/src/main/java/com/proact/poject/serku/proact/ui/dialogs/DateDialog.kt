package com.proact.poject.serku.proact.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DateDialog : DialogFragment() {
    var calendar = Calendar.getInstance()
    lateinit var dateListener: (year: Int, month: Int, day: Int) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val listener = DatePickerDialog.OnDateSetListener { _, daeteYear, dateMonth, dayOfMonth ->
            dateListener(daeteYear, dateMonth, dayOfMonth)
        }

        return DatePickerDialog(activity!!, listener, year, month, day).apply {
            datePicker.minDate = calendar.timeInMillis
        }
    }
}