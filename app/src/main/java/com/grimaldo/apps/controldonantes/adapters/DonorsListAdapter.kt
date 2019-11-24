package com.grimaldo.apps.controldonantes.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.grimaldo.apps.controldonantes.R
import com.grimaldo.apps.controldonantes.domain.User

class DonorsListAdapter(val context: Context, private val donors: List<User>, private val onClickListener: OnClickDonorListener) :
    RecyclerView.Adapter<DonorsListAdapter.DonorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DonorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.donor_list_item, parent, false)
        return DonorViewHolder(view)
    }

    override fun getItemCount(): Int = donors.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(donorViewHolder: DonorViewHolder, position: Int) {
        val donor = donors[position]
        donorViewHolder.apply {
            tvUsername.text = donor.getFullName()
            tvId.text = "Id: ${donor.id}"
            tvAge.text = "Edad: ${donor.age}"
            tvBlood.text = "Sangre ${donor.getFullBlood()}"
            tvWeight.text = "Peso: ${donor.weight} Kg"
            tvHeight.text = "Estatura: ${donor.height} cm"
            btnEditDonor.setOnClickListener {
                onClickListener.onClickEditDonor(donor)
            }
            btnDeleteDonor.setOnClickListener {
                onClickListener.onClickDeleteDonor(donor)
            }
        }
    }

    class DonorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvName)
        val tvId: TextView = view.findViewById(R.id.tvId)
        val tvAge: TextView = view.findViewById(R.id.tvAge)
        val tvBlood: TextView = view.findViewById(R.id.tvBlood)
        val tvWeight: TextView = view.findViewById(R.id.tvWeight)
        val tvHeight: TextView = view.findViewById(R.id.tvHeight)
        val btnEditDonor: ImageButton = view.findViewById(R.id.btnEditDonor)
        val btnDeleteDonor: ImageButton = view.findViewById(R.id.btnDeleteDonor)
    }

    interface OnClickDonorListener {
        fun onClickEditDonor(donor: User)
        fun onClickDeleteDonor(donor: User)
    }
}