package com.example.derscalisma.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.derscalisma.R
import com.example.derscalisma.databinding.FragmentKronometreBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class kronometreFragment : Fragment() {

    private var _binding: FragmentKronometreBinding? = null
    private val binding get() = _binding!!
    private var seconds =0
    private var kayit=0
    private var timerJob: Job? = null
    private var saat=0
    private var dakika=0
    private var saniye=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        _binding = FragmentKronometreBinding.inflate(inflater, container, false)
        val view = binding.root
        return view      }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBasla.setOnClickListener { basla() }
        binding.buttonSifirla.isEnabled=false
        binding.buttonSifirla.setOnClickListener { sifirla() }

    }
    private fun kronometer (){
        if (timerJob?.isActive != true) {
            timerJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(1000)
                seconds++
                dakika = (seconds/60)%60
                saat = seconds/3600
                saniye = seconds%60
                binding.textView3.text = String.format("%02d:%02d.%02d", saat, dakika, saniye)
            }
        }
        }
    }
    fun basla(){
        kronometer()
        binding.buttonBasla.text="Dur"
        binding.buttonBasla.setOnClickListener { dur() }
    }
    fun dur(){
        timerJob?.cancel()
        kayit=seconds
        binding.buttonSifirla.isEnabled=true
        binding.buttonBasla.text="Devam et"
        binding.buttonBasla.setOnClickListener { devam() }

    }
    fun sifirla(){
        seconds=0
        binding.textView3.text = "00:00.00"
        binding.buttonBasla.text="Başla"
        binding.buttonBasla.setOnClickListener { basla() }
        binding.buttonSifirla.isEnabled=false

    }
    fun devam(){
        seconds = kayit
        kronometer()
        binding.buttonBasla.text="Dur"
        binding.buttonSifirla.isEnabled=false
        binding.buttonBasla.setOnClickListener { dur() }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

