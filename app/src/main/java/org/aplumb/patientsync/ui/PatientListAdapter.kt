package org.aplumb.patientsync.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.patient_item.view.*
import org.aplumb.patientsync.R
import org.aplumb.patientsync.sql.PatientWithPulse

class PatientListAdapter(
    private val navigateToPatientDetail: (String) -> Unit
) :
    RecyclerView.Adapter<PatientListAdapter.ViewHolder>() {

    var patients: List<PatientWithPulse> = emptyList()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val patient = v.tag as PatientWithPulse
            navigateToPatientDetail(patient.uuid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.patient_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val patient = patients[position]
        holder.nameView.text = patient.name
        holder.heartRateView.text = patient.heartRate?.let { "%.1f".format(it) } ?: ""

        with(holder.itemView) {
            tag = patient
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = patients.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.patient_name
        val heartRateView: TextView = view.heart_rate
    }
}