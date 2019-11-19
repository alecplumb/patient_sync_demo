package org.aplumb.patientsync.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pulse_item.view.*
import org.aplumb.patientsync.R
import org.aplumb.patientsync.sql.PatientPulse
import org.aplumb.patientsync.utils.fromIsoToDate
import org.aplumb.patientsync.utils.heartRateTimeDisplayFormat

class PulseListAdapter :
    RecyclerView.Adapter<PulseListAdapter.ViewHolder>() {

    var pulses: List<PatientPulse> = emptyList()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pulse_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pulse = pulses[position]
        holder.timestampView.text =
            heartRateTimeDisplayFormat.format(pulse.timestamp.fromIsoToDate())
        holder.heartRateView.text = pulse.heartRate?.let { "%.1f".format(it) } ?: ""

        with(holder.itemView) {
            tag = pulse
        }
    }

    override fun getItemCount() = pulses.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timestampView: TextView = view.timestamp
        val heartRateView: TextView = view.heart_rate
    }
}