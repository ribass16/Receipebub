package com.example.pap

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pap.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Perfil : Fragment() {

    private lateinit var binding: FragmentPerfilBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val refUser = db.collection("utilizadores").document(userId)
        refUser.get().addOnSuccessListener {
            if (it != null) {
                val nomePerfil = it.data?.get("nome").toString()

                binding.edtNome.setText(nomePerfil)
            }
        }

        binding.guardar.setOnClickListener {
            val gNome = binding.edtNome.text.toString()

            val mapUser = mapOf(
                "nome" to gNome
            )
            db.collection("utilizadores").document(userId).update(mapUser)
                .addOnSuccessListener {
                    Toast.makeText(context, R.string.Nomealteradocomsucesso, Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, R.string.Erroaoalterarnome, Toast.LENGTH_SHORT).show()
                }
        }

        binding.sairconta.setOnClickListener {
            auth.signOut()
            requireActivity().finish()
        }

        binding.eliminarconta.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle(R.string.Confirmação)
            alertDialogBuilder.setMessage(R.string.Temcertezadequedesejaexcluirasuaconta)
            alertDialogBuilder.setPositiveButton(R.string.Sim) { _, _ ->
                eliminarconta()
            }
            alertDialogBuilder.setNegativeButton(R.string.Cancelar) { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        buscarNome()
    }

    private fun eliminarconta() {
        val handler = Handler()
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val userId = user?.uid
        if (userId != null) {
            db.collection("utilizadores").document(userId).delete()
            db.collection("receitas").document(userId).delete()
        }
        user?.delete()
            ?.addOnSuccessListener {
                Toast.makeText(context, R.string.Contaeliminadacomsucesso, Toast.LENGTH_SHORT)
                    .show()
                handler.postDelayed({
                    val i = Intent(context, LoginActivity::class.java)
                    startActivity(i)
                }, 1000)
            }
            ?.addOnFailureListener {
                Toast.makeText(context, R.string.Erroaoeliminarconta, Toast.LENGTH_SHORT).show()
            }
    }


    private fun buscarNome() {
        val id = auth.currentUser?.uid
        if (id != null) {
            db.collection("utilizadores").document(id).get()
                .addOnSuccessListener { document ->
                    val dados = document.data
                    if (dados != null) {
                        val nome = dados["nome"].toString()
                        binding.tvname.text = nome
                    }
                }
        }
    }
}
