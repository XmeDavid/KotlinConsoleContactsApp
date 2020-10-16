import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * The goal is to have a contact system,
 * With a menu where the user can choose what kind of operation he wants to do
 * the options should at least be the CRUD options (Create Read Update Delete), a few more may be introduced later
 * TODO Optional : Save contacts to a file and load them later
 * TODO Optional : Make a log file where all the operations are registered with a timestamp
 */

data class Contact(var id: Int, var name: String, var number: Long, var email: String){
    override fun toString(): String { return "Contact $id - [Name: $name, Phone Number: $number, Email: $email]" }
}

fun getUserInputL(message: String):Long{ print("$message "); return readLine()!!.toLong() }
fun getUserInputS(message: String):String{ print("$message "); return readLine()!! }

fun createContact(numberOfContacts: Int): Contact{ return Contact(numberOfContacts,getUserInputS("Name:"),getUserInputL("Phone Number:"),getUserInputS("Email:")) }

fun getIndexByNumber(contacts: MutableList<Contact>): Int {
    val number:Long = getUserInputL("Phone number:")
    contacts.forEachIndexed{i, it -> if (it.number == number) return i }
    return -1
}

fun updateContact(contacts: MutableList<Contact>, index: Int): Contact {
    var auxContact: Contact
    auxContact = contacts[index]

    when (getUserInputS("What do you want to update?\n(A)ll\n(N)ame\n(P)hone\n(E)mail\nChoice: ").toLowerCase()) {
        "all", "a"  -> { auxContact = createContact(contacts[index].id) }
        "name", "n" -> { auxContact.name = getUserInputS("Name:") }
        "phone", "p" -> { auxContact.number = getUserInputL("Phone Number:") }
        "email", "e" -> { auxContact.email = getUserInputS("Email:") }
    }
    return auxContact
}

fun containsContact(contacts: MutableList<Contact>, contact: Contact) : Boolean{
    contacts.forEach {
        if (it.name == contact.name) return true
        if (it.number == contact.number) return true
        if (it.email == contact.email) return true
    }
    return false
}

fun mergeContacts(contactsLocal: MutableList<Contact>, contactsFile: MutableList<Contact>): MutableList<Contact>{
    val localContactsSize = contactsLocal.size
    contactsFile.forEach eachFile@{
        if(containsContact(contactsLocal,it)) return@eachFile
        it.id = it.id + localContactsSize
        contactsLocal.add(it)
    }
    return contactsLocal
}

fun main(args: Array<String>) {
    var exit: Boolean = false
    var numberOfContacts: Int = 1
    var contacts = mutableListOf<Contact>()
    while(!exit){
        var userInputMainMenu:String = getUserInputS("What do you want to do? \n(C)reate\n(S)earch\n(L)ist\n(U)pdate\n(D)elete\n(F)ile save\n(R)ead from file\n(E)xit\nChoice: ")
        when(userInputMainMenu.toLowerCase()){
            "create", "c" -> { println("Entering new Contact!"); contacts.add(createContact(numberOfContacts++)); log("Contact Created: ${contacts[numberOfContacts-2]}]")}
            "search", "s" -> { println("Search for Contact"); println(contacts[getIndexByNumber(contacts)].toString())}
            "list", "l"   -> { println("List of Contacts"); contacts.forEach{println(it.toString())}; log("Listed $numberOfContacts Contacts")}
            "update", "u" -> { println("Update a Contact"); val index = getIndexByNumber(contacts); contacts[index] = updateContact(contacts, index)}
            "delete", "d" -> { println("Delete a contact"); log("Contact Removed : ${contacts.removeAt(getIndexByNumber(contacts))}"); numberOfContacts-- }
            "read", "r", "load"   -> { contacts = mergeContacts(contacts,loadContactsFromFile()); numberOfContacts=contacts.size + 1 ; log("Loaded $numberOfContacts Contacts to program")}
            "file", "f", "save", "s"   -> { saveContactsToFile(contacts); log("Saved $numberOfContacts to file") }
            "exit", "e"   -> exit = true
            else ->{
                println("Option not avalible")
            }
        }
    }
}
