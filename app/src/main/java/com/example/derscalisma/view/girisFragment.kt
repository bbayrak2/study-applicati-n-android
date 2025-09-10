package com.example.derscalisma.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.derscalisma.databinding.FragmentGirisBinding
import com.example.derscalisma.model.Soz
import com.example.derscalisma.roomdb.SozDAO
import com.example.derscalisma.roomdb.SozDatabase
import kotlinx.coroutines.launch


class girisFragment : Fragment() {
    private var _binding: FragmentGirisBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: SozDatabase
    private lateinit var sozDao :SozDAO

    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(),SozDatabase::class.java,"Sozler").allowMainThreadQueries()
            .build()
        sozDao=db.sozDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        _binding = FragmentGirisBinding.inflate(inflater, container, false)
        val view = binding.root
        return view      }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val cumleler = listOf(
            "Başarı sabırla gelir",
            "Düşmek yenilgi değil, vazgeçmek yenilgidir",
            "Küçük adımlar büyük yolculuklar başlatır",
            "Hayallerine giden yolda pes etme",
            "Zorluklar seni güçlendirir",
            "Bugün attığın adım yarınını şekillendirir",
            "Başlamak, başarmanın yarısıdır",
            "Umudunu kaybetme, mucizeler her gün olur",
            "Her gün yeni bir başlangıçtır",
            "Azimle çalış, sonuç kendiliğinden gelir",
            "Hayallerin için mücadele et",
            "Başarısızlık, başarı yolunda bir derstir",
            "Kendi ışığını parlat, başkaları yolunu bulsun",
            "Güçlü olmak, zor zamanlarda belli olur",
            "Hayat, cesur olanları ödüllendirir",
            "İnandığın sürece imkânsız yoktur",
            "Sabır, başarının gizli anahtarıdır",
            "Her zorluk seni biraz daha büyütür",
            "Yavaş da olsa ilerlemek, durmaktan iyidir",
            "Kendine inan, gerisi zaten gelir"
        )

        lifecycleScope.launch {
            cumleler.forEach { metin ->
                val soz = Soz(cumle = metin)
                val list= sozDao.getAll()
                if (list.isEmpty()){
                sozDao.insert(soz)}
            }
            val liste = sozDao.getAll()
            if (liste.isNotEmpty()) {
                val rastgeleCumle = liste.random()
                binding.textView10.text = rastgeleCumle.cumle}
        }

        binding.button3.setOnClickListener { program(it) }
        binding.button.setOnClickListener { sayac(it) }
        binding.button2.setOnClickListener { kronometre(it) }
        binding.buttonAyarlar.setOnClickListener { ayarlar(it) }


        val switch1=prefs.getBoolean("sw1",false)
        val switch2=prefs.getBoolean("sw2",false)
        val switch3=prefs.getBoolean("sw3",false)


        if (switch1){

        }else{
            binding.textView10.visibility=View.GONE
        }

        if (switch3){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    fun ayarlar(view: View){
        val action=girisFragmentDirections.actionGirisFragmentToAyarlarFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun sayac(view: View){
        val action=girisFragmentDirections.actionGirisFragmentToSayacFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun kronometre(view: View){
        val action = girisFragmentDirections.actionGirisFragmentToKronometreFragment()
        Navigation.findNavController(view).navigate(action)

    }
    fun program(view: View){
        val action = girisFragmentDirections.actionGirisFragmentToListeFragment()
        Navigation.findNavController(view).navigate(action)
    }


    override fun onDestroy() {
        super.onDestroy()
    }


}