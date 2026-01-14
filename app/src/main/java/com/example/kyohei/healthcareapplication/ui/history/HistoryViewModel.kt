package com.example.kyohei.healthcareapplication.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kyohei.healthcareapplication.HealthRecord
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    private val realm: Realm
) : ViewModel() {

    val recordsUi = realm.query<HealthRecord>()
        .sort("createdAt", Sort.DESCENDING)
        .asFlow()
        .filterIsInstance<ResultsChange<HealthRecord>>()
        .map { change -> change.list.map { it.toUi() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
