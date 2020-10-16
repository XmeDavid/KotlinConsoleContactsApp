import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Had problems trying to get a Gson library Clueless how to do it,
 * So I made a custome solution to save the data in a file
 */


const val dataFile = "data.txt"
const val logFile = "contacts.log"

/**
 * saveContactsToFile, stringifyContact and stringifyStringArray functions
 * This are the functions required to save a mutable list of contacts to a file
 * only call the saveContactsToFile(contacts) to use it.
 * This function will create an array of strings, each string wil contain the info of a contact as string,
 * this contact to string is done with the stringifyContact function, it will convert the data in the contact to a string following a custome format
 * this format is not a convention, it takes inspiration from json, BUT IT IS NOT JSON
 * Then after collecting the array of this strings for each contact it will call the stringifyStringArray
 * This function will group all the contacts as strings, into one string, and it will write that to a file
 */

fun saveContactsToFile(contacts: MutableList<Contact>) {
    var contactsAsString: MutableList<String> = mutableListOf()
    contacts.forEach { contactsAsString.add(stringifyContact(it)) }
    File(dataFile).writeText(stringifyStringArray(contactsAsString))
}

fun stringifyContact(contact: Contact): String{
    return "{${contact.id},${contact.name},${contact.number},${contact.email}}"
}

fun stringifyStringArray(objects: MutableList<String>): String{
    var finalStr: String = "["
    var numberOfObjs: Int = objects.size;
    var i: Int = 1
    for (obj in objects) {
        if (i == numberOfObjs){
            finalStr += "$obj"
            finalStr += "]"
            return finalStr
        }
        finalStr += "$obj,"
        ++i
    }
    return ""
}

/**
 * loadContactsFromFile and readContactFromString functions
 *
 */

private fun readContactFromString(string: String): Contact{
    var id:String = ""
    var name: String = ""
    var number: String = ""
    var email: String = ""

    var whatAmIReading: Int = 0
    string.forEach {
        if (it == '{'){
            whatAmIReading = 1
        }else {
            when (whatAmIReading) {
                1 -> {
                    if (it != ',') {
                        id += it
                    } else {
                        whatAmIReading=2
                    }
                }
                2 -> {
                    if (it != ',') {
                        name += it
                    } else {
                        whatAmIReading=3
                    }
                }
                3 -> {
                    if (it != ',') {
                        number += it
                    } else {
                        whatAmIReading=4
                    }
                }
                4 -> {
                    if (it != ',') {
                        email += it
                    } else {
                        whatAmIReading=5
                    }
                }
            }
        }
    }
    return Contact(id.toInt(), name, number.toLong(), email)
}

fun loadContactsFromFile(): MutableList<Contact> {
    var contactList: MutableList<Contact> = mutableListOf()
    var jsonContacts = File(dataFile).readText()
    var charRead: Char = ' '
    var startIndex: Int = -1
    var charIndex: Int = 0
    while(charRead != ']'){
        charRead = jsonContacts[charIndex]
        if (charRead == '{')
            startIndex = charIndex
        if (charRead == '}' && startIndex != -1){
            contactList.add(readContactFromString(jsonContacts.substring(startIndex,charIndex)))
            startIndex = -1
        }
        charIndex++
    }
    return contactList
}


/**
 * log function
 * This functions pupose is to log information to a .log file
 * It takesa message to log as input and it writes it to file along with current date and time
 */
fun log(message:String){
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    val logMessage = "[" + current.format(formatter) + "] - " + message + "\n"
    File(logFile).appendText(logMessage)
}