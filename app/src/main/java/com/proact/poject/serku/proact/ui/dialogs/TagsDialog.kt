package com.proact.poject.serku.proact.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.proact.poject.serku.proact.R

class TagsDialog : DialogFragment() {
    lateinit var positiveButtonListener: () -> Unit
    val tagsList = mutableListOf<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.tags_dialog_title)
            .setMultiChoiceItems(R.array.tags_array, null
            ) { _, checkboxNumber, isChecked ->
                val title = getTagString(checkboxNumber)

                if (isChecked && !tagsList.contains(title)) {
                    tagsList.add(title)
                } else if (!isChecked) {
                    tagsList.remove(title)
                }
            }
            .setPositiveButton(R.string.ok_button) { _, _ ->
                positiveButtonListener()
            }

        return builder.create()
    }

    private fun getTagString(checkBoxNumber: Int) = when (checkBoxNumber) {
        0 -> "Фронтенд"
        1 -> "Бэкенд"
        2 -> "Веб-дизайн"
        3 -> "Andorid"
        4 -> "IOS-разработка"
        5 -> "Маркетинг"
        6 -> "SMM"
        else -> ""
    }
}