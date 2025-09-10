package com.example.derscalisma.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.derscalisma.databinding.FragmentAyarlarBinding
import com.example.derscalisma.model.Soz
import kotlinx.coroutines.launch


class AyarlarFragment : Fragment() {
    private var _binding: FragmentAyarlarBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


        binding.switch7.isChecked = prefs.getBoolean("sw1", false)

        binding.switch9.isChecked = prefs.getBoolean("sw3", false)


        binding.button5.setOnClickListener {

            prefs.edit().apply {
                putBoolean("sw1", binding.switch7.isChecked)

                putBoolean("sw3", binding.switch9.isChecked)
                apply()
            }


            val action = AyarlarFragmentDirections.actionAyarlarFragmentToGirisFragment(
                switch1 = binding.switch7.isChecked,

                switch3 = binding.switch9.isChecked
            )
            findNavController().navigate(action)
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAyarlarBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

