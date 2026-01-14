package com.example.kyohei.healthcareapplication.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kyohei.healthcareapplication.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter :
    ListAdapter<HealthRecordUi, HistoryAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<HealthRecordUi>() {
            override fun areItemsTheSame(oldItem: HealthRecordUi, newItem: HealthRecordUi) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: HealthRecordUi, newItem: HealthRecordUi) =
                oldItem == newItem
        }

        private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN)
    }

    class ViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.textDate.text = dateFormat.format(Date(item.createdAt))
        holder.binding.textBp.text = "血圧: ${item.max}/${item.min}  脈拍: ${item.pulse}"

        val bmiText = item.bmi?.let { String.format(Locale.JAPAN, "%.1f", it) } ?: "-"
        val weightText = String.format(Locale.JAPAN, "%.1f", item.weightKg)

        holder.binding.textBody.text =
            "身長: ${item.heightCm}cm  体重: ${weightText}kg  BMI: $bmiText"
    }
}
