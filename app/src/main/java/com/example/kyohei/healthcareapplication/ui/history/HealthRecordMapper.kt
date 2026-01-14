package com.example.kyohei.healthcareapplication.ui.history

import com.example.kyohei.healthcareapplication.HealthRecord

private fun calcBmi(heightCm: Int, weightKg: Float): Float? {
    if (heightCm <= 0) return null
    val h = heightCm / 100f
    if (h <= 0f) return null
    return weightKg / (h * h)
}

fun HealthRecord.toUi(): HealthRecordUi {
    return HealthRecordUi(
        id = _id.toHexString(),
        createdAt = createdAt,
        max = max,
        min = min,
        pulse = pulse,
        heightCm = heightCm,
        weightKg = weightKg,
        bmi = calcBmi(heightCm, weightKg)
    )
}
