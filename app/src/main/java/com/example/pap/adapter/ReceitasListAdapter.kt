package com.example.pap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pap.R
import com.example.pap.model.Receitas
import com.squareup.picasso.Picasso

class ReceitasListAdapter(
    private val listareceitas: List<Receitas>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<ReceitasListAdapter.ReceitasViewHolder>() {

    class ReceitasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textview)
        val categoria: TextView = itemView.findViewById(R.id.textcategoria1)
        val foto: ImageView = itemView.findViewById(R.id.image_foto_row)
    }

    class OnClickListener(val clickListener: (receitas: Receitas) -> Unit) {
        fun onClick(receitas: Receitas) = clickListener(receitas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitasViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.receitas_rcylerview, parent, false)

        return ReceitasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listareceitas.size
    }

    override fun onBindViewHolder(holder: ReceitasViewHolder, position: Int) {
        val receitas = listareceitas[position]
        holder.textView.text = receitas.titulo
        holder.categoria.text = receitas.categoria
        Picasso.get()
            .load(receitas.foto)
            .into(holder.foto)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(receitas)
        }
    }
}
