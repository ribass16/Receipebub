package com.example.pap

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pap.databinding.FragmentAdicionarBinding

class AdicionarFragment : Fragment() {
    private var _binding: FragmentAdicionarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdicionarBinding.inflate(inflater, container, false)
        return binding.root
    }


}