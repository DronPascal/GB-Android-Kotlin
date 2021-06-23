package com.pascal.weatherapp.ui.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pascal.weatherapp.R
import com.pascal.weatherapp.data.model.Contact
import com.pascal.weatherapp.databinding.ContactsActivityBinding


class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ContactsActivityBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var weatherMsg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        weatherMsg = intent.extras?.getString(ARGUMENT_WEATHER_MSG) ?: ""

        initView()
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    private fun initView() {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() {
        with(binding.contactsRecycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initRvAdapter(contacts: ArrayList<Contact>) {
        contactsAdapter = ContactsAdapter(contacts)
        contactsAdapter.setItemClickListener(object : ContactsAdapter.OnItemClickListener {
            override fun onItemClick(contact: Contact) {
                val uri = Uri.parse("smsto:${contact.number}")
                val smsIntent = Intent(Intent.ACTION_SENDTO, uri)
                smsIntent.putExtra("sms_body", "Привет, ${contact.name}! $weatherMsg")
                smsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(smsIntent)
            }
        })
        binding.contactsRecycler.adapter = contactsAdapter
        contactsAdapter.notifyDataSetChanged()
    }

    private fun checkPermission() {
        this.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.contacts_permission_dialog_title))
                        .setMessage(getString(R.string.contacts_permission_dialog_description))
                        .setPositiveButton(getString(R.string.contacts_permission_dialog_allow)) { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton(getString(R.string.contacts_permission_dialog_deny)) { dialog, _ ->
                            dialog.dismiss()
                            this@ContactsActivity.onBackPressed()
                        }
                        .create()
                        .show()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    private fun getContacts() {
        this.let {
            val contentResolver: ContentResolver = it.contentResolver
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            val contacts: MutableList<Contact> = ArrayList()
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val contactId =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                        val phones: Cursor? = contentResolver.query(
                            Phone.CONTENT_URI, null,
                            Phone.CONTACT_ID + " = " + contactId, null, null
                        )
                        phones?.let {
                            while (phones.moveToNext()) {
                                when (phones.getInt(phones.getColumnIndex(Phone.TYPE))) {
                                    Phone.TYPE_MOBILE -> {
                                        val number =
                                            phones.getString(phones.getColumnIndex(Phone.NUMBER))
                                        contacts.add(Contact(name, number))
                                    }
                                }
                            }
                        }
                        phones?.close()
                    }
                }
            }
            cursorWithContacts?.close()
            initRvAdapter(contacts as ArrayList<Contact>)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getContacts()
                } else {
                    this.let {
                        AlertDialog.Builder(it)
                            .setTitle(getString(R.string.contacts_permission_dialog_title))
                            .setMessage(getString(R.string.contacts_denied_dialog_description))
                            .setPositiveButton(getString(R.string.contacts_permission_dialog_settings)) { _, _ ->
                                val dialogIntent = Intent()
                                dialogIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                val uri = Uri.fromParts("package", packageName, null)
                                dialogIntent.data = uri
                                startActivity(dialogIntent)
                            }
                            .setNegativeButton(getString(R.string.close)) { dialog, _ ->
                                dialog.dismiss()
                                this@ContactsActivity.onBackPressed()
                            }
                            .create()
                            .show()
                    }
                }
                return
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    companion object {
        const val REQUEST_CODE = 1001
        const val ARGUMENT_WEATHER_MSG = "weather_msg"
    }

}