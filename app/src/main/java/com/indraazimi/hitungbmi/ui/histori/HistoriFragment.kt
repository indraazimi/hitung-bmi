/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 1.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.hitungbmi.ui.histori

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.indraazimi.hitungbmi.databinding.FragmentHistoriBinding
import com.indraazimi.hitungbmi.db.BmiDb

class HistoriFragment : Fragment() {

    private val viewModel: HistoriViewModel by lazy {
        val db = BmiDb.getInstance(requireContext())
        val factory = HistoriViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HistoriViewModel::class.java]
    }

    private lateinit var binding: FragmentHistoriBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHistoriBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.data.observe(viewLifecycleOwner, {
            Log.d("HistoriFragment", "Jumlah data: ${it.size}")
        })
    }
}