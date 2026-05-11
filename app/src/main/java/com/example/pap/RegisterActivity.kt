package com.example.pap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pap.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvEntrar.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        auth = Firebase.auth

        binding.btnRegistar.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister() {
        val inputREmail = binding.edtREmail.text.toString().trim()
        val inputRSenha = binding.edtRSenha.text.toString().trim()
        val inputRNome = binding.edtRNome.text.toString().trim()

        if (inputRNome.isEmpty() || inputREmail.isEmpty() || inputRSenha.isEmpty()) {
            Toast.makeText(this, R.string.Preenchaoscamposemfalta, Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(inputREmail, inputRSenha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                    Toast.makeText(this, R.string.Verefiqueoseuemailparaconseguiravancar, Toast.LENGTH_SHORT).show()
                    salvarUtilizador(inputRNome, inputREmail)
                } else {
                    Toast.makeText(baseContext, R.string.Autenticacaosemsucesso, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "${getString(R.string.Ocorreuumerro)}: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun salvarUtilizador(nome: String, email: String) {
        val id = auth.currentUser?.uid
        if (id != null) {
            val dados = mapOf(
                "nome" to nome,
                "email" to email,
            )
            db.collection("utilizadores").document(id).set(dados)
        }
    }
}