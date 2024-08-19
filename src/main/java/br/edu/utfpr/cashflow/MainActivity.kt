package br.edu.utfpr.cashflow

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.cashflow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onResume() {
        super.onResume()

        setTypeAdapter()
    }

    private fun updateSources() {
        val spinnerArray =
            when (binding.tilType.editText?.text.toString()) {
                "Debit" -> R.array.debit_sources_array
                "Credit" -> R.array.credit_sources_array
                else -> null
            }

        binding.tilSource.isEnabled = spinnerArray != null

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
        setTypeAdapter()
        binding.spType.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            updateSources()
        }
        updateSources()
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