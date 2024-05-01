package com.example.mobile_prog_midterm

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

class FormActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var spinGender: Spinner
    private lateinit var etNik: EditText
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etTempat: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etUmur: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnReset: MaterialButton
    private lateinit var rbWna: RadioButton
    private lateinit var rbWni: RadioButton
    private lateinit var llKeahlian: LinearLayout
    private lateinit var rgNasionalitas: RadioGroup

    private lateinit var elNik: TextInputLayout
    private lateinit var elNama: TextInputLayout
    private lateinit var elAlamat: TextInputLayout
    private lateinit var elTempat: TextInputLayout
    private lateinit var elTanggal: TextInputLayout
    private lateinit var elUmur: TextInputLayout
    private lateinit var elEmail: TextInputLayout
    private lateinit var erNasionalitas: TextView
    private lateinit var erKompetensi: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinGender = findViewById(R.id.spin_gender)
        etNik = findViewById(R.id.et_nik)
        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        etTempat = findViewById(R.id.et_tempat)
        etTanggal = findViewById(R.id.et_tanggal)

        etUmur = findViewById(R.id.et_umur)
        etEmail = findViewById(R.id.et_email)
        rbWna = findViewById(R.id.rb_wna)
        rbWni = findViewById(R.id.rb_wni)
        btnReset = findViewById(R.id.btn_reset)
        btnSubmit = findViewById(R.id.btn_submit)
        llKeahlian = findViewById(R.id.ll_keahlian)
        rgNasionalitas = findViewById(R.id.rg_nasionalitas)

        elNik = findViewById(R.id.input_nik)
        elNama = findViewById(R.id.input_nama)
        elTanggal = findViewById(R.id.input_tanggal_lahir)
        elAlamat = findViewById(R.id.input_alamat)
        elTempat = findViewById(R.id.input_tempat_lahir)
        elTanggal = findViewById(R.id.input_tanggal_lahir)
        elEmail = findViewById(R.id.input_email)
        elUmur = findViewById(R.id.input_umur)

        ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item)
            .also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinGender.adapter = adapter
            }

        btnSubmit.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        etTanggal.setOnClickListener(this)

        elTanggal.setEndIconOnClickListener { createDatePicker() }

        setKompetensi()
        nikValidator()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_submit -> {
                submitForm()
            }

            R.id.btn_reset -> {
                resetForm()
            }
        }
    }

    private fun nikValidator() {
        etNik.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (s?.count()!! > 16) elNik.error = "NIK lebih dari 16 digit!"
                else if (s.count() < 16) elNik.error = "NIK kurang dari 16 digit!"
                else elNik.error = null
            }
        })
    }

    private fun setKompetensi() {
        val keahlian = arrayOf(
                "Development & IT",
                "AI Service",
                "Creative Design",
                "Writing",
                "Finance & Accounting"
            )

        for (text in keahlian) {
            val checkbox = MaterialCheckBox(this)
            checkbox.text = text
            llKeahlian.addView(checkbox)
        }
    }

    private fun getKompetensi(): List<String> {
        val selectedCheck: MutableList<String> = mutableListOf()

        for (i in 0 until llKeahlian.childCount) {
            val view = llKeahlian.getChildAt(i)
            if (view is CheckBox) {
                if (view.isChecked) {
                    selectedCheck.add(view.text.toString())
                }
            }
        }

        return selectedCheck
    }

    private fun resetKompetensi() {
        for (i in 0 until llKeahlian.childCount) {
            val view = llKeahlian.getChildAt(i)
            if (view is CheckBox) {
                if (view.isChecked) {
                    view.setChecked(false)
                }
            }
        }
    }

    private fun createDatePicker() {
        val currentDate: Calendar = Calendar.getInstance()
        val currYear = currentDate.get(Calendar.YEAR)
        val currMonth = currentDate.get(Calendar.MONTH)
        val currDay = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view: DatePicker, year: Int, month: Int, day: Int ->
                // Display the selected date in the TextView
                etTanggal.setText("$day/${month + 1}/$year")
                val umur = countAge(day, month, year)
                Log.d("duhTest", umur.toString())

                if (umur > 0) {
                    etUmur.setText("${umur} Tahun")
                } else {
                    Toast.makeText(this, "Tanggal tidak valid", Toast.LENGTH_SHORT).show()
                }
            },
            currYear,
            currMonth,
            currDay
        )

        datePickerDialog.show()
    }

    private fun countAge(day: Int, month: Int, year: Int): Int {
        val birthDate = LocalDate.of(year, month, day)
        val currentDate = LocalDate.now()

        val count = Period.between(birthDate, currentDate)

        return count.years
    }

    private fun getUmur(): String {
        val umur: List<String> = etUmur.text.toString().split(" ")
        return umur[0]
    }

    private fun getKewarganegaraan(): String {
        return rbWna.isChecked.let {
            if (it) "WNA" else if (rbWni.isChecked) "WNI" else "N/A"
        }
    }

    private fun validateForm(
        nik: String,
        nama: String,
        tanggal: String,
        tempat: String,
        alamat: String,
        usia: String,
        gender: String,
        kewarganegaraan: String,
        kompetensi: List<String>,
        email: String
    ): Boolean {
        var errorBag = 0

        if (nik.isEmpty() || nik.count() in 15..17) {
            elNik.error = "NIK kosong!"
            errorBag += 1
        }

        if (nama.isEmpty()) {
            elNama.error = "Nama kosong!"
            errorBag += 1
        }

        if (tanggal.isEmpty()) {
            //elTanggal.error = "Tanggal lahir kosong!"
            errorBag += 1
        }

        if (tempat.isEmpty()) {
            elTempat.error = "Tempat lahir kosong!"
            errorBag += 1
        }

        if (alamat.isEmpty()) {
            elAlamat.error = "Alamat kosong!"
            errorBag += 1
        }

        if (usia.isEmpty()) {
            elUmur.error = "Usia kosong!"
            errorBag += 1
        }

        Log.d("testDuh", usia.isEmpty().toString())

        if (email.isEmpty()) {
            elEmail.error = "Email kosong!"
            errorBag += 1
        }

        if (errorBag > 0) return false

        return true
    }

    private fun submitForm() {
        val nik: String = etNik.text.toString()
        val nama: String = etNama.text.toString()
        val tanggal: String = etTanggal.text.toString()
        val tempat: String = etTempat.text.toString()
        val alamat: String = etAlamat.text.toString()
        val usia: String = getUmur()
        val gender: String = spinGender.selectedItem.toString()
        val kewarganegaraan: String = getKewarganegaraan()
        val kompetensi: List<String> = getKompetensi()
        val email: String = etEmail.text.toString()

        val gson = Gson()
        val kompetensiFinal: String = gson.toJson(kompetensi)

        val validation: Boolean = validateForm(nik, nama, tanggal, tempat, alamat, usia, gender, kewarganegaraan, kompetensi, email)

        if (validation) {
            val sharedPreference = getSharedPreferences("uts_pref", Context.MODE_PRIVATE)
            val prefEditor = sharedPreference.edit()

            prefEditor.putString("nik", nik)
            prefEditor.putString("nama", nama)
            prefEditor.putString("tanggal", tanggal)
            prefEditor.putString("tempat", tempat)
            prefEditor.putString("alamat", alamat)
            prefEditor.putString("usia", usia)
            prefEditor.putString("gender", gender)
            prefEditor.putString("kewarganegaraan", kewarganegaraan)
            prefEditor.putString("kompetensi", kompetensiFinal)
            prefEditor.putString("email", email)

            prefEditor.apply()

            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun resetForm() {
        etNik.text = null
        etTempat.text = null
        etAlamat.text = null
        etUmur.text = null
        etEmail.text = null
        etTanggal.text = null
        etNama.text = null

        spinGender.setSelection(0)
        rgNasionalitas.clearCheck()
        resetKompetensi()
    }
}
