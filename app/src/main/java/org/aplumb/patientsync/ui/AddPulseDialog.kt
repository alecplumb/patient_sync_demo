package org.aplumb.patientsync.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.dialog_add_pulse_content.view.*
import org.aplumb.patientsync.PatientSyncApp
import org.aplumb.patientsync.R
import org.aplumb.patientsync.repository.PatientRepository
import javax.inject.Inject

class AddPulseDialog() : DialogFragment() {

    @Inject
    lateinit var repository: PatientRepository

    var contentView: View? = null

    private val patientUuid
        get() = arguments?.get(PatientPulseDetailFragment.ARG_PATIENT_UUID) as String?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PatientSyncApp.component().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            contentView = it.layoutInflater.inflate(R.layout.dialog_add_pulse_content, null)

            // Set the dialog title
            builder.setTitle(R.string.add_patient)
                .setView(contentView)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                // Set the action buttons
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // TODO: Add Validations

                    val appContext = activity?.applicationContext
                    val heartRate = contentView
                        ?.heart_rate_edit
                        ?.text
                        ?.toString()
                        ?.toDoubleOrNull() ?: 0.0
                    patientUuid?.let { patientUuid ->
                        repository.addPulse(patientUuid, heartRate)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                Toast
                                    .makeText(appContext, "Added Pulse", Toast.LENGTH_LONG)
                                    .show()
                            }
                    }
                    dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        /**
         * The fragment argument representing the patient uuid that this fragment
         * represents.
         */
        const val ARG_PATIENT_UUID = "patient_uuid"

        fun newInstance(patientUuid: String): AddPulseDialog {
            return AddPulseDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PATIENT_UUID, patientUuid)
                }
            }
        }
    }
}