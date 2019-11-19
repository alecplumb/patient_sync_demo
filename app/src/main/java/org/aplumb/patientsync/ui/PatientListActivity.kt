package org.aplumb.patientsync.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_patient_list.*
import kotlinx.android.synthetic.main.patient_list.*
import org.aplumb.patientsync.PatientSyncApp
import org.aplumb.patientsync.R
import org.aplumb.patientsync.di.ViewModelFactory
import javax.inject.Inject

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PatientPulseDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class PatientListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: PatientListViewModel
    lateinit var adapter: PatientListAdapter

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PatientSyncApp.component().inject(this)
        setContentView(R.layout.activity_patient_list)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PatientListViewModel::class.java)

        viewModel.patients.observe(
            this,
            Observer { adapter.patients = it })
        viewModel.updatePatients()

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener {
            AddPatientDialog().show(supportFragmentManager, "add_patient")
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        adapter = PatientListAdapter(::navigateToPatientDetail)
        patient_list.adapter = adapter
    }

    private fun navigateToPatientDetail(patientUuid: String) {
        if (twoPane) {
            val fragment = PatientPulseDetailFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(PatientPulseDetailFragment.ARG_PATIENT_UUID, patientUuid)
                    }
                }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, PatientPulseDetailActivity::class.java).apply {
                putExtra(PatientPulseDetailFragment.ARG_PATIENT_UUID, patientUuid)
            }
            startActivity(intent)
        }
    }

}
