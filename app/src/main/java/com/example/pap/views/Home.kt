package com.example.pap.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pap.InfoActivity
import com.example.pap.R
import com.example.pap.TelaIngredientes
import com.example.pap.adapter.ReceitasListAdapter
import com.example.pap.model.Receitas
import com.google.firebase.firestore.FirebaseFirestore

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var receitasListAdapter: ReceitasListAdapter

    private val db by lazy { FirebaseFirestore.getInstance() }

    private val listaReceitas by lazy { mutableListOf<Receitas>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onResume() {
        super.onResume()
        getListOfReceitas()
    }

    private fun getListOfReceitas() {
        db.collection("receitas").addSnapshotListener { querySnapshot, error ->
            listaReceitas.clear()
            val documents = querySnapshot?.documents
            if (documents != null) {
                for (doc in documents) {
                    val id = doc.id
                    val titulo = doc.get("titulo").toString()
                    val porcao = doc.get("porcaoingredientes").toString()
                    val modo = doc.get("modoprep").toString()
                    val categoria = doc.get("categoria").toString()
                    val tempo = doc.get("tempo").toString()
                    val notas = doc.get("notas").toString()
                    val foto = doc.get("foto").toString()

                    listaReceitas.add(Receitas(id, titulo, porcao, modo, categoria, tempo, notas, foto))
                }

                receitasListAdapter =
                    ReceitasListAdapter(listaReceitas, ReceitasListAdapter.OnClickListener {
                        val i = Intent(requireContext(), InfoActivity::class.java)
                        i.putExtra("receitaID", it.id)
                        startActivity(i)
                    })

                try {
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = receitasListAdapter
                } catch (_: Exception) {

                }
            }
        }


    }

}