package com.example.user.android.model

import android.content.Context
import android.provider.ContactsContract
import android.util.Log

class ContactRepository(private val context: Context) {
    private val contacts: List<Contact>
    fun fetchContacts(): List<Contact> {
        var contact: Contact
        //hold a list of Contacts without duplicates
        val cleanList: MutableMap<String, Contact> = LinkedHashMap()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor?.count ?: 0 > 0) {
            while (cursor!!.moveToNext()) {
                contact = Contact()
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNo =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                Log.e("contact", "getAllContacts: $name $phoneNo $photoUri")
                contact.name = name
                contact.phoneNumber = formatPhoneNumber(phoneNo)
                contact.photoUri = photoUri
                //contacts.add(contact);
                cleanList[contact.phoneNumber!!] = contact
            }
        }
        cursor?.close()
        return ArrayList(cleanList.values)
    }



    companion object {
        //Format Phone Number
        private fun formatPhoneNumber(phone: String): String {
            var formatedPhone = phone.replace(" ".toRegex(), "")
            val phoneNumberLength = formatedPhone.length
            if (phoneNumberLength == 13) {
                formatedPhone = "0" + formatedPhone.substring(4)
            } else if (phoneNumberLength == 12) {
                formatedPhone = "0" + formatedPhone.substring(3)
            } else if (phoneNumberLength == 10) {
                return formatedPhone
            }
            return formatedPhone
        }
    }

    init {
        contacts = ArrayList()
    }
}