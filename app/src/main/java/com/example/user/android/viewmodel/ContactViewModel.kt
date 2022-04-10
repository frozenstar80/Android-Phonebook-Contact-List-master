package com.example.user.android.viewmodel

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import com.example.user.android.model.Contact
import com.example.user.android.model.ContactRepository

class ContactViewModel(context: Context?) : BaseObservable() {
    val contacts: ObservableArrayList<Contact> = ObservableArrayList()
    private val repository: ContactRepository? = context?.let { ContactRepository(it) }
    fun getContacts(): List<Contact> {
        repository?.fetchContacts()?.let { contacts.addAll(it) }
        return contacts
    }

}