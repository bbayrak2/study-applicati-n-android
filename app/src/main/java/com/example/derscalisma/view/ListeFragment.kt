package com.example.derscalisma.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.derscalisma.adapter.ProgramAdapter
import com.example.derscalisma.databinding.FragmentListeBinding
import com.example.derscalisma.model.Program
import com.example.derscalisma.roomdb.ProgramDAO
import com.example.derscalisma.roomdb.ProgramDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListeFragment : Fragment() {

    private var _binding: FragmentListeBinding? = null

    private val binding get() = _binding!!
    private lateinit var db: ProgramDatabase
    private lateinit var programDao: ProgramDAO
    private val mDisposable =CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(),ProgramDatabase::class.java,"Programlar").build()
        programDao =db.programDao()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener{yeniEkle(it)}
        binding.programRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        verileriAl()
    }
    private fun verileriAl(){
        mDisposable.add(
            programDao.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }
    private fun handleResponse(programlar:List<Program>){
        val adapter = ProgramAdapter(programlar)
        binding.programRecyclerView.adapter = adapter
    }


    fun yeniEkle(view: View){
        val action =ListeFragmentDirections.actionListeFragmentToProgramFragment2(bilgi = "yeni",id=0)
        Navigation.findNavController(view).navigate(action)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}