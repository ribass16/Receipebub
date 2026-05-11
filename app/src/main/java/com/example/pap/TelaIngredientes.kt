package com.example.pap

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.pap.databinding.ActivityTelaIngredientesBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar

class TelaIngredientes : AppCompatActivity() {


    private lateinit var binding: ActivityTelaIngredientesBinding

    private lateinit var uri: Uri

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaIngredientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selecionaImagemGaleria =
            registerForActivityResult(ActivityResultContracts.GetContent()) {
                if (it != null) {
                    binding.imageViewFoto.setImageURI(it)
                    uri = it
                    binding.imageViewFoto.visibility = View.VISIBLE
                    binding.btnfoto.text = getString(R.string.Adicionarfoto)
                } else {
                    binding.imageViewFoto.visibility = View.GONE
                }
            }

        binding.btnfoto.setOnClickListener {
            selecionaImagemGaleria.launch("image/*")
        }

        binding.sair.setOnClickListener {
            finish()
        }
        binding.btnadd.setOnClickListener {
            showTimePickerDialog()
        }

        binding.button2.setOnClickListener {
            val titulo = binding.edtTitulo.text.toString().trim()
            val porcao = binding.edtPorcao.text.toString().trim()
            val tempo = binding.edtTempo.text.toString().trim()
            val modo = binding.edtModo.text.toString().trim()
            val notas = binding.edtNota.text.toString().trim()

            if (titulo.isNotEmpty() && porcao.isNotEmpty() && tempo.isNotEmpty() && modo.isNotEmpty() && notas.isNotEmpty()) {
                if (::uri.isInitialized) {
                    salvarReceita(titulo, porcao, tempo, modo, notas)
                    Toast.makeText(this, R.string.Receitaadicionadacomsucesso, Toast.LENGTH_SHORT).show()
                    Handler(mainLooper).postDelayed({
                        finish()
                    }, 1500)
                } else {
                    Toast.makeText(this, R.string.Adicionarfoto, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.Preenchaoscamposemfalta, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showTimePickerDialog() {

        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->

                binding.edtTempo.setText(String.format("%02d:%02d", hourOfDay, minute))
            }, hour, minute, true
        )


        timePickerDialog.show()
    }

    private fun salvarReceita(
        tituloR: String,
        porcaoR: String,
        tempoR: String,
        modoR: String,
        notasR: String
    ) {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val current = formatter.format(time)

        val id = auth.currentUser?.uid
        if (id != null) {

            storage.getReference("receitas").child(id).child(current).putFile(uri)
                .addOnSuccessListener { task ->
                    task.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { url ->
                            val dados = mapOf(
                                "uid" to id,
                                "titulo" to tituloR,
                                "porcaoingredientes" to porcaoR,
                                "tempo" to tempoR,
                                "modoprep" to modoR,
                                "notas" to notasR,
                                "foto" to url
                            )
                            db.collection("receitas")
                                .add(dados)
                        }
                }
        }
    }
}
