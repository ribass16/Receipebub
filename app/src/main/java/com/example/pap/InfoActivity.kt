package com.example.pap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import com.example.pap.databinding.ActivityInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AlertDialog

import com.squareup.picasso.Picasso

class InfoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityInfoBinding.inflate(layoutInflater) }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.sair.setOnClickListener {
            finish()
        }

        val i = intent
        val receitaID = i.extras?.getString("receitaID")

        if (receitaID != null) {
            val refUser = db.collection("receitas").document(receitaID)
            refUser.get().addOnSuccessListener {
                if (it != null) {
                    val titulo = it.data?.get("titulo")?.toString()
                    val porcao = it.data?.get("porcaoingredientes")?.toString()
                    val tempo = it.data?.get("tempo")?.toString()
                    val modoprep = it.data?.get("modoprep")?.toString()
                    val notas = it.data?.get("notas")?.toString()
                    val foto = it.data?.get("foto")?.toString()

                    binding.ifTitulo.setText(titulo)
                    binding.ifPorcao.setText(porcao)

                    binding.ifTempo.setText(tempo)
                    binding.ifModo.setText(modoprep)
                    binding.ifNota.setText(notas)
                    binding.ifPorcao.setText("$porcao\n")
                    binding.ifModo.setText("$modoprep\n")
                    binding.ifNota.setText("$notas\n")

                    binding.ifTitulo.inputType = InputType.TYPE_NULL
                    binding.ifPorcao.inputType = InputType.TYPE_NULL
                    binding.ifTempo.inputType = InputType.TYPE_NULL
                    binding.ifModo.inputType = InputType.TYPE_NULL
                    binding.ifNota.inputType = InputType.TYPE_NULL

                    Picasso.get()
                        .load(foto)
                        .into(binding.imagem)
                }
            }
        }

        binding.btnEditar.setOnClickListener {
            binding.ifTitulo.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            binding.ifPorcao.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            binding.ifTempo.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            binding.ifModo.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            binding.ifNota.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE

            Toast.makeText(this, R.string.japodeescrever, Toast.LENGTH_SHORT).show()
        }


        binding.btnSalvar.setOnClickListener {
            val infoTituloS = binding.ifTitulo.text.toString().trim()
            val infoPorcaoS = binding.ifPorcao.text.toString().trim()
            val infoTempoS = binding.ifTempo.text.toString().trim()
            val infoModoS = binding.ifModo.text.toString().trim()
            val infoNotaS = binding.ifNota.text.toString().trim()

            val updateReceita = mapOf(
                "titulo" to infoTituloS,
                "porcaoingredientes" to infoPorcaoS,
                "modoprep" to infoModoS,
                "tempo" to infoTempoS,
                "notas" to infoNotaS
            )

            if (receitaID != null) {
                db.collection("receitas").document(receitaID).update(updateReceita)
                    .addOnCompleteListener {
                        Toast.makeText(this, R.string.Sucesso, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, R.string.Semsucesso, Toast.LENGTH_SHORT).show()
                    }
            }

        }

        binding.btnApagar.setOnClickListener {
            if (receitaID != null) {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle(R.string.Confirmação)
                alertDialogBuilder.setMessage(R.string.Temcertezadequedesejaapagarestareceita)

                alertDialogBuilder.setPositiveButton("Sim") { dialog, which ->
                    db.collection("receitas").document(receitaID).delete()
                        .addOnCompleteListener {
                            Toast.makeText(this, R.string.Receitaapagadacomsucesso, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, R.string.Falhaaoapagarareceita, Toast.LENGTH_SHORT).show()
                        }
                }

                alertDialogBuilder.setNegativeButton("Não") { dialog, which ->

                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
            }
        }
    }
