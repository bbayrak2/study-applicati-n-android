package com.example.derscalisma.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.lifecycle.lifecycleScope
import com.example.derscalisma.R
import com.example.derscalisma.databinding.FragmentSayacBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class sayacFragment : Fragment() {

    private var _binding: FragmentSayacBinding? = null
    private val binding get() = _binding!!
    private var totalSeconds:Int =0
    private var saat =0
    private var dakika=0
    private var saniye =0
    private var kayit =0
    private var timerJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSayacBinding.inflate(inflater, container, false)
        val view = binding.root
        return view    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linearLayout.post{
            val with1 = binding.linearLayout2.width
            val params =binding.linearLayout.layoutParams
            params.width=with1
            binding.linearLayout.layoutParams=params

        }
        binding.textView8.visibility =View.GONE
        binding.buttonSifirla.visibility =View.GONE
        binding.button8.setOnClickListener { basla() }
        binding.buttonSifirla.setOnClickListener { sifirla() }
        with(binding) {
            // Saat: 0-23
            numberPickerHour.minValue = 0
            numberPickerHour.maxValue = 99
            numberPickerHour.wrapSelectorWheel = true
            numberPickerHour.setFormatter { i -> String.format("%02d", i) }

            numberPickerMinute.minValue = 0
            numberPickerMinute.maxValue = 59
            numberPickerMinute.wrapSelectorWheel = true
            numberPickerMinute.setFormatter { i -> String.format("%02d", i) }

            numberPickerSecond.minValue = 0
            numberPickerSecond.maxValue = 59
            numberPickerSecond.wrapSelectorWheel = true
            numberPickerSecond.setFormatter { i -> String.format("%02d", i) }

            val onChangeListener = NumberPicker.OnValueChangeListener { _, _, _ ->
                 totalSeconds = numberPickerHour.value * 3600 +
                        numberPickerMinute.value * 60 +
                        numberPickerSecond.value
            }


            numberPickerHour.setOnValueChangedListener(onChangeListener)
            numberPickerMinute.setOnValueChangedListener(onChangeListener)
            numberPickerSecond.setOnValueChangedListener(onChangeListener)
        }
    }
        private fun kronometer (){
            if (timerJob?.isActive != true) {
                timerJob = viewLifecycleOwner.lifecycleScope.launch {
                    while (isActive && totalSeconds>=0) {
                        delay(1000)
                        totalSeconds--
                        dakika = (totalSeconds/60)%60
                        saat = totalSeconds/3600
                        saniye = totalSeconds%60
                        binding.textView8.text = String.format("%02d:%02d.%02d", saat, dakika, saniye)
                    }
                }
            }
        }

    private fun basla(){
        kronometer()
        binding.linearLayout2.visibility =View.GONE
        binding.linearLayout.visibility =View.GONE
        binding.textView8.visibility=View.VISIBLE
        binding.button8.text="Dur"
        binding.button8.setOnClickListener { dur() }
    }fun dur(){
        timerJob?.cancel()
        kayit = totalSeconds
        binding.buttonSifirla.isEnabled=true
        binding.button8.text="Devam et"
        binding.button8.setOnClickListener { devam() }
        binding.buttonSifirla.visibility=View.VISIBLE

    }
    fun sifirla(){
        binding.textView8.text="00:00.0"
        totalSeconds = binding.numberPickerHour.value * 3600 +
                binding.numberPickerMinute.value * 60 +
                binding.numberPickerSecond.value
        binding.linearLayout.visibility=View.VISIBLE
        binding.linearLayout2.visibility=View.VISIBLE
        binding.button8.text="Başla"
        binding.button8.setOnClickListener { basla() }
        binding.buttonSifirla.visibility=View.GONE
        binding.textView8.visibility=View.GONE

    }
    fun devam(){
        totalSeconds = kayit
        kronometer()
        binding.button8.text="Dur"
        binding.buttonSifirla.isEnabled=false
        binding.button8.setOnClickListener { dur() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







