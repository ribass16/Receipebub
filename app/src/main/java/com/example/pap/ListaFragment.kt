import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pap.R
import com.google.firebase.firestore.FirebaseFirestore
import com.example.pap.databinding.FragmentListaBinding

class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val itemList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemList)
        binding.listViewItems.adapter = adapter

        carregarItensDoFirestore()

        binding.buttonAdd.setOnClickListener {
            val itemText = binding.editTextItem.text.toString().trim()
            if (itemText.isNotEmpty()) {
                salvarItemNoFirebase(itemText)
                binding.editTextItem.text?.clear()
            }
        }

        binding.listViewItems.setOnItemClickListener { _, _, position, _ ->
            val notaSelecionada = itemList[position]
            mostrarDialogoConfirmacaoEliminarNota(notaSelecionada)
        }

        binding.buttonLimpar.setOnClickListener {
            mostrarDialogoConfirmacao()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mostrarDialogoConfirmacaoEliminarNota(nota: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmação")
        builder.setMessage("Tem certeza que deseja eliminar esta nota?")
        builder.setPositiveButton("Sim") { _, _ ->
            eliminarNotaDoFirestore(nota)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun eliminarNotaDoFirestore(nota: String) {
        db.collection("notas")
            .whereEqualTo("nota", nota)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    db.collection("notas").document(document.id).delete()
                        .addOnSuccessListener {
                            carregarItensDoFirestore()
                        }
                        .addOnFailureListener { e ->

                        }
                }
            }
            .addOnFailureListener { exception ->

            }
    }

    private fun mostrarDialogoConfirmacao() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmação")
        builder.setMessage("Tem certeza que deseja limpar a lista?")
        builder.setPositiveButton("Sim") { dialogInterface: DialogInterface, _: Int ->
            limparListaNoFirestore()
        }
        builder.setNegativeButton("Não") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun salvarItemNoFirebase(itemText: String) {
        val notasmap = mapOf("nota" to itemText)
        db.collection("notas").add(notasmap)
            .addOnSuccessListener {

                carregarItensDoFirestore()
            }
            .addOnFailureListener { e ->

            }
    }

    private fun carregarItensDoFirestore() {
        db.collection("notas").get()
            .addOnSuccessListener { querySnapshot ->
                itemList.clear()
                for (document in querySnapshot) {
                    val nota = document.getString("nota")
                    if (nota != null) {
                        itemList.add(nota)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->

            }
    }

    private fun limparListaNoFirestore() {
        db.collection("notas").get()

            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {

                    db.collection("notas").document(document.id).delete()
                        .addOnSuccessListener {

                            carregarItensDoFirestore()
                        }
                        .addOnFailureListener { e ->

                        }
                }
            }
            .addOnFailureListener { exception ->

            }
    }
}