
package com.example.contacts

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val contacts = mutableListOf(
        "yassmine naouar",
        "Islem bahri",
        "adem ouarhani",
        "ayoub maiza",
        "mouhamed aziz",
        "obaid",
        "basmala ouida",
        "ghada naouar"
    )

    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var etContact: EditText
    private lateinit var btnModifier: Button
    private lateinit var btnAjouter: Button
    private lateinit var lvContacts: ListView

    private var positionSelectionnee = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etContact = findViewById(R.id.etContact)
        btnModifier = findViewById(R.id.btnModifier)
        btnAjouter = findViewById(R.id.btnAjouter)
        lvContacts = findViewById(R.id.lvContacts)

        // Restaurer les données
        if (savedInstanceState != null) {
            val savedList = savedInstanceState.getStringArrayList(KEY_CONTACTS)
            if (savedList != null) {
                contacts.clear()
                contacts.addAll(savedList)
            }

            positionSelectionnee =
                savedInstanceState.getInt(KEY_SELECTION, -1)
        }

        // Adapter personnalisé
        adapter = object : ArrayAdapter<String>(
            this,
            R.layout.item_contact,
            R.id.tvContactName,
            contacts
        ) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getView(position, convertView, parent)

                val tvAvatar = view.findViewById<TextView>(R.id.tvAvatar)
                val contactName = getItem(position) ?: ""

                tvAvatar.text =
                    contactName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

                // Ne pas bloquer les clics
                view.isClickable = false
                view.isFocusable = false

                return view
            }
        }

        lvContacts.adapter = adapter

        // Configuration de la sélection
        lvContacts.choiceMode = ListView.CHOICE_MODE_SINGLE
        lvContacts.isClickable = true
        lvContacts.isEnabled = true

        // Restaurer la sélection
        if (positionSelectionnee in contacts.indices) {
            lvContacts.setItemChecked(positionSelectionnee, true)
            etContact.setText(contacts[positionSelectionnee])
        }

        // Sélectionner un contact
        lvContacts.setOnItemClickListener { _, _, position, _ ->

            positionSelectionnee = position

            val contact = contacts[position]

            etContact.setText(contact)
            etContact.setSelection(etContact.text.length)

            lvContacts.setItemChecked(position, true)

            Toast.makeText(
                this,
                "Contact sélectionné : $contact",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Modifier
        btnModifier.setOnClickListener {
            modifierContact()
        }

        // Ajouter
        btnAjouter.setOnClickListener {
            ajouterContact()
        }

        // Supprimer avec clic long
        lvContacts.setOnItemLongClickListener { _, _, position, _ ->

            val nomASupprimer = contacts[position]

            AlertDialog.Builder(this)
                .setTitle(R.string.titre_confirmation_suppression)
                .setMessage(
                    getString(
                        R.string.msg_confirmation_suppression,
                        nomASupprimer
                    )
                )
                .setPositiveButton(R.string.btn_supprimer) { _, _ ->

                    contacts.removeAt(position)

                    if (positionSelectionnee == position) {
                        positionSelectionnee = -1
                        etContact.text.clear()
                        lvContacts.clearChoices()

                    } else if (positionSelectionnee > position) {
                        positionSelectionnee--
                        lvContacts.clearChoices()
                        lvContacts.setItemChecked(positionSelectionnee, true)
                    }

                    adapter.notifyDataSetChanged()

                    Toast.makeText(
                        this,
                        "Contact supprimé : $nomASupprimer",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()

            true
        }

        // Bouton Terminé du clavier
        etContact.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                modifierContact()
                true
            } else {
                false
            }
        }
    }

    // Modifier un contact
    private fun modifierContact() {

        if (positionSelectionnee !in contacts.indices) {
            Toast.makeText(
                this,
                R.string.msg_aucune_selection,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val nouveauNom = etContact.text.toString().trim()

        if (nouveauNom.isEmpty()) {
            etContact.error = getString(R.string.msg_nom_vide)
            return
        }

        val existeDeja = contacts.indices.any {
            it != positionSelectionnee &&
                    contacts[it].equals(nouveauNom, ignoreCase = true)
        }

        if (existeDeja) {
            Toast.makeText(
                this,
                R.string.msg_doublon,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        contacts[positionSelectionnee] = nouveauNom

        // Tri alphabétique
        contacts.sort()

        // Retrouver la nouvelle position
        positionSelectionnee = contacts.indexOf(nouveauNom)

        adapter.notifyDataSetChanged()

        lvContacts.clearChoices()
        lvContacts.setItemChecked(positionSelectionnee, true)

        Toast.makeText(
            this,
            "Contact modifié : $nouveauNom",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Ajouter un contact
    private fun ajouterContact() {

        val nom = etContact.text.toString().trim()

        if (nom.isEmpty()) {
            etContact.error = getString(R.string.msg_nom_vide)
            return
        }

        if (contacts.any {
                it.equals(nom, ignoreCase = true)
            }) {
            Toast.makeText(
                this,
                R.string.msg_doublon,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        contacts.add(nom)
        contacts.sort()

        adapter.notifyDataSetChanged()

        etContact.text.clear()
        positionSelectionnee = -1
        lvContacts.clearChoices()

        Toast.makeText(
            this,
            "Contact ajouté : $nom",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Sauvegarder l'état
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList(
            KEY_CONTACTS,
            ArrayList(contacts)
        )

        outState.putInt(KEY_SELECTION, positionSelectionnee)
    }

    companion object {
        private const val KEY_CONTACTS = "key_contacts"
        private const val KEY_SELECTION = "key_selection"
    }
}
