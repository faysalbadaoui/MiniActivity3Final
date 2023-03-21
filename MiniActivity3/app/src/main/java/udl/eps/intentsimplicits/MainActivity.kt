package udl.eps.intentsimplicits

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import udl.eps.intentsimplicits.databinding.ActivityMainBinding

class MainActivity : ComponentActivity(), View.OnClickListener {

    private lateinit var readContactsReqPermLaunc: ActivityResultLauncher<String>
    private lateinit var callsReqPermLaunc: ActivityResultLauncher<String>
    val PICK_IMAGE_REQUEST = 1
    lateinit var imageView: ImageView

    private val PICK_CONTACT_REQUEST = 2
    private lateinit var textView: TextView

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btn1 = binding.button1
        val btn2 = binding.button2
        val btn3 = binding.button3
        val btn4 = binding.button4
        val btn5 = binding.button5
        val btn6 = binding.button6
        val btn7 = binding.button7
        val btn8 = binding.button8
        val btn9 = binding.button9
        val btn10 = binding.button10
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)
        btn9.setOnClickListener(this)
        btn10.setOnClickListener(this)
        readContactsReqPermLaunc = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                accessContactsAction()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                showSnackbar(
                    R.string.permission_denied_explanation,
                    R.string.settings
                ) { // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID , null
                    )  // Amb la darrera API level deprecated. Ara és packageName
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        callsReqPermLaunc = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                callPhoneAction()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision.
                showSnackbar(
                    R.string.permission_denied_explanation,
                    R.string.settings
                ) { // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID , null
                    )  // Amb la darrera API level deprecated. Ara és packageName
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        //if (!ckeckPermissions()) requestPermissions()
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        val lat = getString(R.string.lat)
        val lon = getString(R.string.lon)
        val url = getString(R.string.url)
        val address = getString(R.string.direccion)
        val textToSearch = getString(R.string.textoABuscar)
        when (v.id) {
            R.id.button1 -> locateByCoordinates(lat, lon)
            R.id.button2 -> locateByAddress(address)
            R.id.button3 -> accessWeb(url)
            R.id.button4 -> seachGoogle(textToSearch)
            R.id.button5 -> callPhoneIfPermissions()
            R.id.button6 -> accessContactsIfPermissions()
            R.id.button7 -> accessDialIfPermissions()
            R.id.button8 -> sendSMSIfPermissions()
            R.id.button9 -> accessSendMAIL()
            R.id.button10 -> accessGallery()
        }
    }



    override fun onResume() {
        super.onResume()
     }

    private fun locateByCoordinates(lat: String, lon: String) {
        Toast.makeText(this, getString(R.string.opcion1), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lon?q=$lat,$lon"))
        startActivity(intent)
    }

    private fun locateByAddress(address: String) {
        Toast.makeText(this, getString(R.string.opcion2), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
        startActivity(intent)
    }

    private fun accessWeb(url: String) {
        val intent: Intent
        Toast.makeText(this, getString(R.string.opcion3), Toast.LENGTH_LONG).show()
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun seachGoogle(textToSearch: String) {
        Toast.makeText(this, getString(R.string.opcion4), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, textToSearch)
        startActivity(intent)
        //Alternative method
        //val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q=$textToSearch"))
    }

    private fun ckeckPermissions(): Boolean {
        return (ckeckPermissionsCallPhone() && ckeckPermissionsReadContacts())
    }

    private fun ckeckPermissionsCallPhone(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED
    }
    private fun ckeckPermissionsSendSMS(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED
    }
    private fun ckeckPermissionsAccessGallery(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
    }
    private fun ckeckPermissionsReadContacts(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE),
            0
        )
    }

    private fun requestPermissionsCallPhone() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.CALL_PHONE
        )
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(
                R.string.callphone_permission_rationale,
                android.R.string.ok
            ) { // Request permission
                callsReqPermLaunc.launch(Manifest.permission.CALL_PHONE)
            }
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission.
            callsReqPermLaunc.launch(Manifest.permission.CALL_PHONE)
        }
    }
    private fun requestPermissionsAccessGallery() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(
                R.string.gallery_permision_text,
                android.R.string.ok
            ) { // Request permission
                callsReqPermLaunc.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission.
            callsReqPermLaunc.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    private fun requestPermissionsSendSMS() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.SEND_SMS
        )
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(
                R.string.send_sms_permision_text,
                android.R.string.ok
            ) { // Request permission
                callsReqPermLaunc.launch(Manifest.permission.SEND_SMS)
            }
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission.
            callsReqPermLaunc.launch(Manifest.permission.SEND_SMS)
        }
    }
    private fun requestPermissionsReadContacts() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_CONTACTS
        )
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(
                R.string.readcontacts_permission_rationale,
                android.R.string.ok
            ) { // Request permission
                readContactsReqPermLaunc.launch(Manifest.permission.CALL_PHONE)
            }
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission.
            readContactsReqPermLaunc.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun accessContactsIfPermissions() {
        if (!ckeckPermissionsReadContacts()) requestPermissionsReadContacts() else accessContactsAction()
    }

    private fun accessContactsAction() {
        Toast.makeText(this, getString(R.string.opcion6), Toast.LENGTH_LONG).show()
        textView = findViewById(R.id.contactName)

        val contactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(contactIntent, PICK_CONTACT_REQUEST)
    }

    private fun callPhoneIfPermissions() {
        if (!ckeckPermissionsCallPhone()) requestPermissionsCallPhone() else callPhoneAction()
    }
    private fun accessDialIfPermissions() {
        if (!ckeckPermissionsCallPhone()) requestPermissionsCallPhone() else accessMakeDial()
    }
    private fun sendSMSIfPermissions() {
        if (!ckeckPermissionsSendSMS()) requestPermissionsSendSMS() else accessSendSMS()
    }
    private fun sendMailIfPermissions() {
        if (!ckeckPermissionsSendSMS()) requestPermissionsSendSMS() else accessSendMAIL()
    }
    private fun accessGallryIfPermission() {
        if (!ckeckPermissionsAccessGallery()) requestPermissionsAccessGallery() else accessGallery()
    }
    private fun callPhoneAction() {
        Toast.makeText(this, getString(R.string.opcion5), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getText(R.string.telef)))
        startActivity(intent)
    }
    private fun accessMakeDial() {
        Toast.makeText(this, getString(R.string.opcion7), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getText(R.string.telef)))
        startActivity(intent)
    }
    private fun accessSendSMS() {
        Toast.makeText(this, getString(R.string.opcion8), Toast.LENGTH_LONG).show()
        val uri = Uri.parse("smsto:0800000123")
        val it = Intent(Intent.ACTION_SENDTO, uri)
        it.putExtra("sms_body", "The SMS text")
        startActivity(it)
    }
    @SuppressLint("QueryPermissionsNeeded")
    private fun accessSendMAIL() {
        Toast.makeText(this, getString(R.string.opcion9), Toast.LENGTH_LONG).show()
        val recipient = "address@address.com"
        val subject = "Subject of the Email"
        val message = "Content of the Email"

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(intent)
    }
    private fun accessGallery() {
        Toast.makeText(this, getString(R.string.opcion10), Toast.LENGTH_LONG).show()
        imageView = findViewById(R.id.imageView)
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            imageView.setImageURI(imageUri)
        }
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
            val contactUri = data.data
            val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
            val cursor = contentResolver.query(contactUri!!, projection, null, null, null)

            if (cursor != null && cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val contactName = cursor.getString(nameIndex)
                textView.text = contactName
            }

            cursor?.close()
        }
    }
    private fun showSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {
        Snackbar.make(
            findViewById(android.R.id.content),
            getString(mainTextStringId),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(actionStringId), listener).show()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}