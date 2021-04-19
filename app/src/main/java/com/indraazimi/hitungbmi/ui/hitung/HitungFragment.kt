/*
 * Copyright (c) 2022 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 1.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.hitungbmi.ui.hitung

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.indraazimi.hitungbmi.R
import com.indraazimi.hitungbmi.databinding.FragmentHitungBinding
import com.indraazimi.hitungbmi.db.BmiDb
import com.indraazimi.hitungbmi.model.HasilBmi
import com.indraazimi.hitungbmi.model.KategoriBmi

class HitungFragment : Fragment() {

    private lateinit var binding: FragmentHitungBinding

    private val viewModel: HitungViewModel by lazy {
        val db = BmiDb.getInstance(requireContext())
        val factory = HitungViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HitungViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHitungBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener { hitungBmi() }
        binding.saranButton.setOnClickListener { viewModel.mulaiNavigasi() }
        binding.shareButton.setOnClickListener { shareData() }

        viewModel.getHasilBmi().observe(requireActivity(), { showResult(it) })
        viewModel.getNavigasi().observe(viewLifecycleOwner, {
            if (it == null) return@observe
            findNavController().navigate(HitungFragmentDirections
                .actionHitungFragmentToSaranFragment(it))
            viewModel.selesaiNavigasi()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_histori -> {
                findNavController().navigate(R.id.action_hitungFragment_to_historiFragment)
                return true
            }
            R.id.menu_about -> {
                findNavController().navigate(R.id.action_hitungFragment_to_aboutFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hitungBmi() {
        val berat = binding.beratEditText.text.toString()
        if (TextUtils.isEmpty(berat)) {
            Toast.makeText(context, R.string.berat_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val tinggi = binding.tinggiEditText.text.toString()
        if (TextUtils.isEmpty(tinggi)) {
            Toast.makeText(context, R.string.tinggi_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(context, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }

        viewModel.hitungBmi(
            berat.toFloat(),
            tinggi.toFloat(),
            selectedId == R.id.priaRadioButton
        )
    }

    private fun showResult(result: HasilBmi?) {
        if (result == null) return

        binding.bmiTextView.text = getString(R.string.bmi_x, result.bmi)
        binding.kategoriTextView.text = getString(R.string.kategori_x,
            getKategoriLabel(result.kategori))
        binding.buttonGroup.visibility = View.VISIBLE
    }

    private fun getKategoriLabel(kategori: KategoriBmi): String {
        val stringRes = when (kategori) {
            KategoriBmi.KURUS -> R.string.kurus
            KategoriBmi.IDEAL -> R.string.ideal
            KategoriBmi.GEMUK -> R.string.gemuk
        }
        return getString(stringRes)
    }

    private fun shareData() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        val gender = if (selectedId == R.id.priaRadioButton)
            getString(R.string.pria)
        else
            getString(R.string.wanita)

        val message = getString(R.string.bagikan_template,
            binding.beratEditText.text,
            binding.tinggiEditText.text,
            gender,
            binding.bmiTextView.text,
            binding.kategoriTextView.text
        )

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }
}