package br.edu.utfpr.cashflow

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.cashflow.database.DatabaseHandler
import br.edu.utfpr.cashflow.database.DatabaseHandler.Companion.CREDIT
import br.edu.utfpr.cashflow.database.DatabaseHandler.Companion.DEBIT
import br.edu.utfpr.cashflow.databinding.ActivityMainBinding
import br.edu.utfpr.cashflow.entities.Entry
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHandler
    private lateinit var datePicker: DatePickerDialog
    private val calendar = Calendar.getInstance()

    override fun onResume() {
        super.onResume()

        setTypeAdapter()
    }

    private fun updateSources() {
        if (binding.spType.text.isNotEmpty().also { binding.tilSource.isEnabled = it }) {
            val spinnerArray =
                when (binding.spType.text.toString().first()) {
                    DEBIT -> R.array.debit_sources_array
                    CREDIT -> R.array.credit_sources_array
                    else -> null
                }

            if (spinnerArray != null) {
                ArrayAdapter.createFromResource(
                    this,
                    spinnerArray,
                    R.layout.spinner_text
                ).also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_text)
                    binding.spSource.setAdapter(arrayAdapter)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = DatabaseHandler(this)
        setTypeAdapter()
        binding.spType.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            updateSources()
        }
        updateSources()

        binding.etLaunchDate.apply {
            isFocusable = false
            isClickable = true
            isFocusableInTouchMode = false
        }

        datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                updateDateInView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        binding.etLaunchDate.setOnClickListener {
            etLaunchDateOnClickListener()
        }

        binding.btSave.setOnClickListener {
            btSaveOnClickListener()
        }

        binding.btEntries.setOnClickListener {
            btEntriesOnClickListener()
        }

        binding.btBalance.setOnClickListener {
            btBalanceOnClickListener()
        }
    }

    private fun etLaunchDateOnClickListener() {
        datePicker.show()
    }

    private fun updateDateInView() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        binding.etLaunchDate.setText(sdf.format(calendar.time))
    }

    @SuppressLint("ShowToast")
    private fun btSaveOnClickListener() {
        if (binding.spType.text.isEmpty()) {
            binding.tilType.error = getString(R.string.field_error)
            binding.tilType.requestFocus()
            return
        }

        if (binding.spSource.text.isEmpty()) {
            binding.tilSource.error = getString(R.string.field_error)
            binding.tilSource.requestFocus()
            return
        }

        if (binding.etValue.text?.isEmpty() == true) {
            binding.tilValue.error = getString(R.string.field_error)
            binding.tilValue.requestFocus()
            return
        }

        if (binding.etLaunchDate.text?.isEmpty() == true) {
            binding.tilLaunchDate.error = getString(R.string.field_error)
            binding.tilLaunchDate.requestFocus()
            return
        }

        db.insert(
            Entry(
                0,
                binding.spType.text[0].toString(),
                binding.spSource.text.toString(),
                binding.etValue.text.toString().toDouble(),
                binding.etLaunchDate.text.toString()
            )
        )

        clearForm()

        Toast.makeText(this, getString(R.string.success_register_message), Toast.LENGTH_LONG).show()
    }

    private fun clearForm() {
        binding.spType.text?.clear()
        binding.spSource.text?.clear()
        binding.etValue.text?.clear()
        binding.etLaunchDate.text?.clear()
        binding.tilType.clearFocus()
        binding.tilSource.clearFocus()
        binding.tilValue.clearFocus()
        binding.tilLaunchDate.clearFocus()
    }

    private fun btEntriesOnClickListener() {
        val intent = Intent(this, Entries::class.java)
        startActivity(intent)
    }

    private fun btBalanceOnClickListener() {
        val balance: Double = db.getBalance()
        val currency: NumberFormat = NumberFormat.getCurrencyInstance()
        currency.maximumFractionDigits = 2

        Toast.makeText(this, "${getString(R.string.balance)}: ${currency.format(balance)}", Toast.LENGTH_LONG).show()
    }

    private fun setTypeAdapter() {
        ArrayAdapter.createFromResource(
            this,
            R.array.types_array,
            R.layout.spinner_text
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_text)
            binding.spType.setAdapter(arrayAdapter)
        }
    }
}