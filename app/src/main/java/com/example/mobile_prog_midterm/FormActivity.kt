package com.example.mobile_prog_midterm

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
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
    private lateinit var elTanggal: TextInputLayout
    private lateinit var etUmur: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnReset: MaterialButton
    private lateinit var rbWna: RadioButton
    private lateinit var rbWni: RadioButton
    private lateinit var llKeahlian: LinearLayout

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
        elTanggal = findViewById(R.id.input_tanggal_lahir)
        etUmur = findViewById(R.id.et_umur)
        etEmail = findViewById(R.id.et_email)
        rbWna = findViewById(R.id.rb_wna)
        rbWni = findViewById(R.id.rb_wni)
        btnReset = findViewById(R.id.btn_reset)
        btnSubmit = findViewById(R.id.btn_submit)
        llKeahlian = findViewById(R.id.ll_keahlian)

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
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_submit -> {
                val nik: String = etNik.text.toString()
                val nama: String = etNama.text.toString()
                val tanggal: String = etTanggal.text.toString()
                val tempat: String = etTempat.text.toString()
                val alamat: String = etAlamat.text.toString()
                val usia: String = getUmur()
                val gender: String = spinGender.selectedItem.toString()
                val kewarganegaraan: String = getKewarganegaraan()
                val kompentensi: List<String> = getKompetensi()
                val email: String = etEmail.text.toString()


            }

            R.id.btn_reset -> {
                Log.d("testDuh", "reset")
                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            }

            R.id.et_tanggal -> {
                //createDatePicker()
            }

            R.id.input_tanggal_lahir -> {
                Log.d("test", "tanggal")
            }
        }
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
}
