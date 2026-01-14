package com.example.kyohei.healthcareapplication.ui.history

data class HealthRecordUi(
    val id: String,
    val createdAt: Long,
    val max: Int,
    val min: Int,
    val pulse: Int,
    val heightCm: Int,
    val weightKg: Float,
    val bmi: Float?
)
