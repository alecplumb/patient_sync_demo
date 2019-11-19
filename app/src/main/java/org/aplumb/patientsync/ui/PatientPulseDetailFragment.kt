package org.aplumb.patientsync.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_patient_pulse_detail.*
import kotlinx.android.synthetic.main.patient_pulse_detail.view.*
import org.aplumb.patientsync.PatientSyncApp
import org.aplumb.patientsync.R
import org.aplumb.patientsync.di.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

/**
 * A fragment representing a single Patient Pulse screen.
 * This fragment is either contained in a [PatientListActivity]
 * in two-pane mode (on tablets) or a [PatientPulseDetailActivity]
 * on handsets.
 */
class PatientPulseDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var viewModel: PatientDetailViewModel? = null
    private lateinit var adapter: PulseListAdapter

    private val patientUuid
        get() = arguments?.get(ARG_PATIENT_UUID) as String?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PatientSyncApp.component().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.patient_pulse_detail, container, false)

        adapter = PulseListAdapter()
        rootView.patient_pulse_list.adapter = adapter

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        patientUuid?.also {
            viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(it, PatientDetailViewModel::class.java)
        } ?: Timber.e("missing patient_uuid!")

        viewModel?.patient?.observe(
            this,
            Observer { patient ->
                activity?.toolbar_layout?.title = patient.name
            })
        viewModel?.pulses?.observe(
            this,
            Observer { adapter.pulses = it })
    }

    override fun onStart() {
        super.onStart()
        patientUuid?.let { viewModel?.updatePatient(it) }
    }

    companion object {
        /**
         * The fragment argument representing the patient uuid that this fragment
         * represents.
         */
        const val ARG_PATIENT_UUID = "patient_uuid"
    }
}
