package com.example.kyohei.healthcareapplication.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kyohei.healthcareapplication.R
import com.example.kyohei.healthcareapplication.RealmProvider
import com.example.kyohei.healthcareapplication.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val adapter = HistoryAdapter()

    private val viewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory(RealmProvider.realm)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentHistoryBinding.bind(view)

        binding.recyclerHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHistory.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recordsUi.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }
}

