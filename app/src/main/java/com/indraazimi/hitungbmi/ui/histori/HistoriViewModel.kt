/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 1.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.hitungbmi.ui.histori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indraazimi.hitungbmi.db.BmiDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoriViewModel(private val db: BmiDao) : ViewModel() {

    val data = db.getLastBmi()

    fun hapusData() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.clearData()
        }
    }
}