package com.example.kyohei.healthcareapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kyohei.healthcareapplication.databinding.FragmentFirstBinding
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val realm = RealmProvider.realm

        // BMI計算（表示用）
        fun calcBmi(heightCm: Int, weightKg: Float): Float? {
            if (heightCm <= 0 || weightKg <= 0f) return null
            val h = heightCm / 100f
            return weightKg / (h * h)
        }

        fun updateBmiLabel() {
            val h = binding.editHeightCm.text.toString().toIntOrNull()
            val w = binding.editWeightKg.text.toString().toFloatOrNull()
            val bmi = if (h != null && w != null) calcBmi(h, w) else null
            binding.textBmi.text = if (bmi == null) "BMI: -" else "BMI: ${String.format("%.1f", bmi)}"
        }

        // 入力が変わったらBMIを更新
        binding.editHeightCm.addTextChangedListener { updateBmiLabel() }
        binding.editWeightKg.addTextChangedListener { updateBmiLabel() }
        updateBmiLabel()

        // 最新データ表示
        fun showLatest() {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val latest = realm.query<HealthRecord>()
                    .sort("createdAt", Sort.DESCENDING)
                    .first()
                    .find()

                if (latest == null) {
                    binding.textviewFirst.text = "まだデータなし"
                    return@launch
                }

                val bmi = calcBmi(latest.heightCm, latest.weightKg)
                val bmiText = bmi?.let { String.format("%.1f", it) } ?: "-"

                binding.textviewFirst.text =
                    "日時: ${android.text.format.DateFormat.format("yyyy/MM/dd HH:mm", latest.createdAt)}\n" +
                            "血圧: ${latest.max}/${latest.min} mmHg  脈拍: ${latest.pulse} bpm\n" +
                            "身長: ${latest.heightCm} cm  体重: ${latest.weightKg} kg  BMI: $bmiText"
            }
        }

        // 初期表示
        showLatest()


        // 保存ボタン
        binding.buttonFirst.setOnClickListener {
            val max = binding.editMax.text.toString().toIntOrNull()
            val min = binding.editMin.text.toString().toIntOrNull()
            val pulse = binding.editPulse.text.toString().toIntOrNull()
            val height = binding.editHeightCm.text.toString().toIntOrNull()
            val weight = binding.editWeightKg.text.toString().toFloatOrNull()

            // 入力チェック
            if (max == null || min == null || pulse == null || height == null || weight == null) {
                binding.textviewFirst.text = "未入力があります（血圧/脈拍/身長/体重を全部入力してね）"
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                realm.write {
                    copyToRealm(
                        HealthRecord().apply {
                            this.max = max
                            this.min = min
                            this.pulse = pulse
                            this.heightCm = height
                            this.weightKg = weight
                            this.createdAt = System.currentTimeMillis()
                            this.message = ""
                        }
                    )
                }

                // 入力欄をクリア
                binding.editMax.setText("")
                binding.editMin.setText("")
                binding.editPulse.setText("")
                binding.editWeightKg.setText("")
                updateBmiLabel()

                // 最新表示更新
                showLatest()


                findNavController().navigate(R.id.action_FirstFragment_to_historyFragment)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
