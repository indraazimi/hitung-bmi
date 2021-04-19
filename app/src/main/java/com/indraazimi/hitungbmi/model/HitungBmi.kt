/*
 * Copyright (c) 2021 Indra Azimi. All rights reserved.
 *
 * Dibuat untuk kelas Pemrograman untuk Perangkat Bergerak 1.
 * Dilarang melakukan penggandaan dan atau komersialisasi,
 * sebagian atau seluruh bagian, baik cetak maupun elektronik
 * terhadap project ini tanpa izin pemilik hak cipta.
 */

package com.indraazimi.hitungbmi.model

import com.indraazimi.hitungbmi.db.BmiEntity

fun BmiEntity.hitungBmi(): HasilBmi {
    val tinggiCm = tinggi / 100
    val bmi = berat / (tinggiCm * tinggiCm)
    val kategori = if (isMale) {
        when {
            bmi < 20.5 -> KategoriBmi.KURUS
            bmi >= 27.0 -> KategoriBmi.GEMUK
            else -> KategoriBmi.IDEAL
        }
    } else {
        when {
            bmi < 18.5 -> KategoriBmi.KURUS
            bmi >= 25.0 -> KategoriBmi.GEMUK
            else -> KategoriBmi.IDEAL
        }
    }
    return HasilBmi(bmi, kategori)
}