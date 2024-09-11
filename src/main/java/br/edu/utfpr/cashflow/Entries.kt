package br.edu.utfpr.cashflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.utfpr.cashflow.adapters.EntriesListAdapter
import br.edu.utfpr.cashflow.database.DatabaseHandler
import br.edu.utfpr.cashflow.databinding.ActivityEntriesBinding

class Entries : AppCompatActivity() {
    private lateinit var binding: ActivityEntriesBinding
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntriesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DatabaseHandler(this)

        readDatabase()
    }

    override fun onStart() {
        super.onStart()
        readDatabase()
    }

    private fun readDatabase() {
        val entries = db.list()

        val adapter = EntriesListAdapter(this, entries)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}