package com.example.mobile_prog_midterm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etNik: EditText
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etTempat: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etUmur: EditText
    private lateinit var etEmail: EditText
    private lateinit var etGender: EditText
    private lateinit var etKompetensi: EditText
    private lateinit var etNasionalitas: EditText
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etNik = findViewById(R.id.et_nik)
        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        etTempat = findViewById(R.id.et_tempat)
        etTanggal = findViewById(R.id.et_tanggal)
        etUmur = findViewById(R.id.et_umur)
        etEmail = findViewById(R.id.et_email)
        etGender = findViewById(R.id.et_gender)
        etKompetensi = findViewById(R.id.et_kompetensi)
        etNasionalitas = findViewById(R.id.et_nasionalitas)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener(this)

        val profile: Map<String, String?> = getProfile()
        setProfile(profile)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back -> {
                val intent = Intent(this, FormActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getProfile(): Map<String, String?> {
        val sharedPreference = getSharedPreferences("uts_pref", Context.MODE_PRIVATE)

        val nik = sharedPreference.getString("nik", null)
        val nama = sharedPreference.getString("nama", null)
        val tanggal = sharedPreference.getString("tanggal", null)
        val tempat = sharedPreference.getString("tempat", null)
        val alamat = sharedPreference.getString("alamat", null)
        val usia = sharedPreference.getString("usia", null)
        val gender = sharedPreference.getString("gender", null)
        val kewarganegaraan = sharedPreference.getString("kewarganegaraan", null)
        val email = sharedPreference.getString("email", null)
        val kompetensi = sharedPreference.getString("kompetensi", null)

        val listType = object : TypeToken<List<String>>() {}.type
        val gson = Gson()
        val kompetensiParsed: List<String> = gson.fromJson(kompetensi, listType)
        val kompetensiFinal: String = kompetensiParsed.joinToString(", ")

        return mapOf(
            "nik" to nik,
            "nama" to nama,
            "tanggal_lahir" to tanggal,
            "tempat_lahir" to tempat,
            "alamat" to alamat,
            "umur" to usia,
            "gender" to gender,
            "kewarganegaraan" to kewarganegaraan,
            "email" to email,
            "kompetensi" to kompetensiFinal
        )
    }

    private fun setProfile(profile: Map<String, String?>) {
        etNik.setText(profile["nik"])
        etNama.setText(profile["nama"])
        etTanggal.setText(profile["tanggal_lahir"])
        etTempat.setText(profile["tempat_lahir"])
        etAlamat.setText(profile["alamat"])
        etUmur.setText(profile["umur"])
        etGender.setText(profile["gender"])
        etNasionalitas.setText(profile["kewarganegaraan"])
        etEmail.setText(profile["email"])
        etKompetensi.setText(profile["kompetensi"])
    }
}