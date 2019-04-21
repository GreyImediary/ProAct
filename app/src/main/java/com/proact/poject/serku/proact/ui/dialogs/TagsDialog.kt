package com.proact.poject.serku.proact.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.data.Tag

class TagsDialog : DialogFragment() {
    lateinit var positiveButtonListener: () -> Unit
    lateinit var tags: MutableList<Tag>
    val checkedTags = mutableListOf<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val tagStrings = tags.map { "${it.category} | ${it.value}" }.toTypedArray()

        builder.setTitle(R.string.tags_dialog_title)
            .setMultiChoiceItems(tagStrings, null
            ) { _, checkboxNumber, isChecked ->
                val tag = tags[checkboxNumber].value

                if (isChecked && !checkedTags.contains(tag)) {
                    checkedTags.add(tag)
                } else if (!isChecked) {
                    checkedTags.remove(tag)
                }
            }
            .setPositiveButton(R.string.ok_button) { _, _ ->
                positiveButtonListener()
            }

        return builder.create()
    }
}