package com.example.pap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pap.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvRegistar.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }



        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val inputLEmail = binding.edtLEmail.text.toString().trim()
        val inputLSenha = binding.edtLSenha.text.toString().trim()

        if (inputLEmail.isEmpty() || inputLSenha.isEmpty()) {
            Toast.makeText(this, R.string.Preenchaoscamposemfalta, Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(inputLEmail, inputLSenha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            R.string.Naoseesquecadeverificaroseuemail,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        baseContext,
                        R.string.Autenticacaosemsucesso,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "${getString(R.string.Ocorreuumerro)}: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        binding.edtLEmail.setText("")
        binding.edtLSenha.setText("")
    }
}