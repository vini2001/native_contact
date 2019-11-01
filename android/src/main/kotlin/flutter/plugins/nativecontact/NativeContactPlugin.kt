package flutter.plugins.nativecontact

import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.content.Intent
import android.provider.ContactsContract
import mapToContact
import android.content.ContentValues
import java.util.ArrayList
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.CommonDataKinds.Website
import NativeContact





class NativeContactPlugin constructor(private val registrar: Registrar): MethodCallHandler, PluginRegistry.ActivityResultListener {

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar): Unit {
            val channel = MethodChannel(registrar.messenger(), "native_contact")
            channel.setMethodCallHandler(NativeContactPlugin(registrar))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result): Unit {
        if (call.method.equals("addNewContact")) {
            addNewContact(mapToContact(call.arguments as Map<String, Any>))
            result.success(null)
        } else {
            result.notImplemented()
        }
    }

    private fun addNewContact(contact: NativeContact) {

        val address = contact.postalAddresses[0]
        val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, contact.name)
            putExtra(ContactsContract.Intents.Insert.COMPANY, contact.company)
            putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contact.jobTitle)
            //if(address != null) putExtra(ContactsContract.Intents.Insert.POSTAL, address.postcode + " " + address.street + ", " + address.region + "/" + address.country)
        }

        val data = ArrayList<ContentValues>()

        for (email in contact.emails) {
            val emailContentValues = ContentValues()
            emailContentValues.put(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
            emailContentValues.put(Email.ADDRESS, email.value)
            emailContentValues.put(Email.LABEL, email.label)
            emailContentValues.put(Email.TYPE, Email.TYPE)
            data.add(emailContentValues)
        }

        for (phone in contact.phones) {
            val phoneContentValues = ContentValues()
            phoneContentValues.put(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
            phoneContentValues.put(Phone.NUMBER, phone.value)
            phoneContentValues.put(Phone.LABEL, phone.label)
            phoneContentValues.put(Phone.TYPE, Phone.TYPE)
            data.add(phoneContentValues)
        }

        for (address in contact.postalAddresses) {
            val postalAddressContentValues = ContentValues()
            postalAddressContentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address.postcode + " " + address.street + ", " + address.neighborhood +  ", " + address.region + "/" + address.country)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.LABEL, address.label)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.CITY, address.city)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, address.country)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, address.postcode)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.REGION, address.region)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD, address.neighborhood)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, address.street)
            postalAddressContentValues.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE)
            data.add(postalAddressContentValues)

            /*val oneLineAddress = ContentValues()
            oneLineAddress.put(ContactsContract.Data.MIMETYPE, ContactsContract.Intents.Insert.POSTAL_TYPE)
            oneLineAddress.put(ContactsContract.Intents.Insert.POSTAL, address.postcode + " " + address.street + ", " + address.region + "/" + address.country)
            oneLineAddress.put(ContactsContract.Intents.Insert.POSTAL_TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
            data.add(oneLineAddress)*/
        }

        val websiteContentValues = ContentValues()
        websiteContentValues.put(ContactsContract.Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
        websiteContentValues.put(Website.URL, contact.website);
        websiteContentValues.put(Website.TYPE, Website.TYPE_HOME);
        data.add(websiteContentValues)



        intent.putExtra(ContactsContract.Intents.Insert.DATA, data)
        //intent.putExtra(ContactsContract.Intents.Insert.POSTAL, "address")

        startIntent(intent)


        /*val addPersonIntent = Intent(Intent.ACTION_INSERT)
        addPersonIntent.type = ContactsContract.Contacts.CONTENT_TYPE

        addPersonIntent.putExtra(ContactsContract.Intents.Insert.NAME, "name")
        addPersonIntent.putExtra(ContactsContract.Intents.Insert.PHONE, "phone")
        addPersonIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, "email")


        startIntent(addPersonIntent)*/


    }

    private fun startIntent(intent: Intent) {
        val context = if (registrar.activity() != null) {
            registrar.activity()
        } else {
            registrar.context()
        }

        context.startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
        return false
    }
}
