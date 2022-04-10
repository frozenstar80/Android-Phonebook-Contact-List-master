package com.example.user.android.view

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.user.android.view.ContactAdapter.ContactViewHolder
import com.example.user.android.model.Contact
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Filter
import com.example.user.contactlist.R
import com.example.user.contactlist.databinding.ItemContactBinding
import java.util.*

class ContactAdapter internal constructor(private val context: Context) :
    RecyclerView.Adapter<ContactViewHolder>() {
    private var contacts: List<Contact>? = null
    private var binding: ItemContactBinding? = null

    // Allows to remember the last item shown on screen
    private val lastPosition = -1
    private var filtered_icontacts: List<Contact> = ArrayList()
    private val mFilter = ItemFilter()
    private var mOnItemClickListener: OnItemClickListener? = null

    /**
     * The interface On item click listener.
     */
    interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param view     the view
         * @param obj      the obj
         * @param position the position
         */
        fun onItemClick(view: View?, obj: Contact?, position: Int)
    }

    /**
     * Sets on item click listener.
     *
     * @param mItemClickListener the m item click listener
     */
    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        filtered_icontacts = contacts
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ContactViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context), R.layout.item_contact,
            viewGroup, false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(contactViewHolder: ContactViewHolder, i: Int) {
        contactViewHolder.onBind(filtered_icontacts[i])
        // Here you apply the animation when the view is bound
    }

    override fun getItemCount(): Int {
        // return contacts != null ? contacts.size() : 0;
        return filtered_icontacts.size
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding?) :
        RecyclerView.ViewHolder(
            binding!!.root
        ) {
        fun onBind(contact: Contact) {
            //binding.setVariable(BR.contact, contact);
            // binding.setContact(contact);
            binding!!.contactName.text = contact.name
            binding.contactNumber.text = contact.phoneNumber
            binding.selectedContactLayout.setOnClickListener { view: View? ->
                mOnItemClickListener!!.onItemClick(
                    view,
                    contact,
                    adapterPosition
                )
            }
            if (contact.photoUri != null) {
                binding.drawableTextView.visibility = View.GONE
                binding.contactPhoto.visibility = View.VISIBLE
                Contact.loadImage(binding.contactPhoto, contact.photoUri)
            } else {
                binding.contactPhoto.visibility = View.GONE
                binding.drawableTextView.visibility = View.VISIBLE
                val gradientDrawable = binding.drawableTextView.background as GradientDrawable
                gradientDrawable.setColor(randomColor)
                val serviceSubString = contact.name?.substring(0, 2)
                binding.drawableTextView.text = serviceSubString?.toUpperCase()
            }
            binding.executePendingBindings()
        }
    }

    /**
     * @return a random color which is used a background by
     * services initial letters
     */
    private val randomColor: Int
        private get() {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

    /**
     * Gets filter.
     *
     * @return the filter
     */
    val filter: Filter
        get() = mFilter

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val query = constraint.toString().toLowerCase()
            val results = FilterResults()
            val list = contacts
            val result_list: MutableList<Contact> = ArrayList(
                list!!.size
            )
            for (i in list.indices) {
                val str_title = list[i].name
                if (str_title != null) {
                    if (str_title.toLowerCase().contains(query)) {
                        result_list.add(list[i])
                    }
                }
            }
            results.values = result_list
            results.count = result_list.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filtered_icontacts = results.values as List<Contact>
            notifyDataSetChanged()
        }
    }
}