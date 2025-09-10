package com.example.derscalisma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.derscalisma.databinding.FragmentGirisBinding
import com.example.derscalisma.databinding.RecyclerRowBinding
import com.example.derscalisma.model.Program
import com.example.derscalisma.view.ListeFragmentDirections

class ProgramAdapter (val programListesi :List<Program>):RecyclerView.Adapter<ProgramAdapter.ProgramHolder>(){

    class ProgramHolder (val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramHolder {

        val recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProgramHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return programListesi.size
    }

    override fun onBindViewHolder(holder: ProgramHolder, position: Int) {
        holder.binding.recyclerViewTextView.text=programListesi[position].isim
        holder.itemView.setOnClickListener{
            val action    =ListeFragmentDirections.actionListeFragmentToProgramFragment2(bilgi ="eski", id = programListesi[position].id )
            Navigation.findNavController(it).navigate(action)

        }

    }


}