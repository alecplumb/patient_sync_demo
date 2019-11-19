package org.aplumb.patientsync.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.dialog_add_patient_content.view.*
import org.aplumb.patientsync.PatientSyncApp
import org.aplumb.patientsync.R
import org.aplumb.patientsync.repository.PatientRepository
import javax.inject.Inject

class AddPatientDialog : DialogFragment() {

    @Inject
    lateinit var repository: PatientRepository

    var contentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PatientSyncApp.component().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            contentView = it.layoutInflater.inflate(R.layout.dialog_add_patient_content, null)

            // Set the dialog title
            builder.setTitle(R.string.add_patient)
                .setView(contentView)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                // Set the action buttons
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // TODO: Add Validations

                    val appContext = activity?.applicationContext
                    val name = contentView?.patient_name_edit?.text?.toString() ?: ""
                    val heartRate = contentView
                        ?.heart_rate_edit
                        ?.text
                        ?.toString()
                        ?.toDoubleOrNull() ?: 0.0
                    repository.addPatient(name, heartRate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            Toast
                                .makeText(appContext, "Added patient $name", Toast.LENGTH_LONG)
                                .show()
                        }
                    dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}