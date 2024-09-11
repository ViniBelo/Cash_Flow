package br.edu.utfpr.cashflow.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.cashflow.R
import br.edu.utfpr.cashflow.database.DatabaseHandler.Companion.CREDIT
import br.edu.utfpr.cashflow.entities.Entry
import java.text.NumberFormat

class EntriesListAdapter(val context: Context, private val cursor: List<Entry>): RecyclerView.Adapter<EntriesListAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        val tvValue: TextView = view.findViewById(R.id.tvValue)
        val tvSource: TextView = view.findViewById(R.id.tvSource)
        val tvLaunchDate: TextView = view.findViewById(R.id.tvLaunchDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.activity_entry_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val entry: Entry = cursor[position]

        holder.ivIcon.setImageResource(if (entry.type[0] == CREDIT) R.drawable.arrow_upward_alt else R.drawable.arrow_downward_alt)

        val currency: NumberFormat = NumberFormat.getCurrencyInstance()
        currency.maximumFractionDigits = 2

        holder.tvValue.text = currency.format(entry.value)
        holder.tvSource.text = entry.source
        holder.tvLaunchDate.text = entry.launchDate
    }

    override fun getItemCount(): Int {
        return cursor.size
    }
}