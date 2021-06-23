package com.pascal.weatherapp.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pascal.weatherapp.R
import com.pascal.weatherapp.data.model.Contact

class ContactsAdapter(
    private val mContactsList: MutableList<Contact>,
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private lateinit var itemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts__item_person_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mContactsList[position].let {
            holder.setData(it)
        }
    }

    override fun getItemCount(): Int = mContactsList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.personName)
        private val numberView: TextView = view.findViewById(R.id.personNumber)

        fun setData(contact: Contact) {
            nameText.text = contact.name
            numberView.text = contact.number

            itemView.setOnClickListener {
                itemClickListener.onItemClick(contact)
            }
        }
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(contact: Contact)
    }
}