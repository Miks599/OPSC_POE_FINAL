package com.example.universaldietandhealthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_notes.*
import java.text.SimpleDateFormat
import java.util.*

class NotesActivity : AppCompatActivity() {

    lateinit var editTextNotes : EditText
    lateinit var ref : DatabaseReference
    val currentuserid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        ref = FirebaseDatabase.getInstance().getReference("users")

        editTextNotes = edtAddNotesHere

        btnUpdateAddedNotes.setOnClickListener{
            //Do something when button is clicked
            saveNotes()
        }

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists())
                {
                    for (u in p0.children)
                    {
                        if (u.child("Profile").exists())
                        {
                            val user = u.child("Profile").getValue(PIData::class.java)

                            if (u.child("Notes").exists())
                            {
                                val notes = u.child("Notes").getValue(NotesData::class.java)

                                if(user!!.currentuserid == currentuserid)
                                {
                                    editTextNotes.setText(notes!!.notes)
                                }
                            }
                        }
                        else
                        {
                            val notes = u.child("Notes").getValue(NotesData::class.java)

                            editTextNotes.setText(notes!!.notes)
                        }
                    }
                }
            }
        })
    }

    private fun saveNotes()
    {
        val notes = editTextNotes.text.toString().trim()

        if (notes.isEmpty())
        {
            editTextNotes.error = "Please enter a note"
            return
        }

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        val note = "Notes"
        val usersnotes = NotesData(currentDate.toString(), notes)

        ref.child(currentuserid).child(note).setValue(usersnotes).addOnCompleteListener {
            Toast.makeText(this, "Notes saved successfully", Toast.LENGTH_LONG).show()
        }
    }
}