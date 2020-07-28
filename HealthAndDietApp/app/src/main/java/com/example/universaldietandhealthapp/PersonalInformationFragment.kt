package com.example.universaldietandhealthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_personalinformation.*
import kotlin.math.roundToInt

class PersonalInformationFragment : Fragment() {

    lateinit var editTextNameSurname : EditText
    lateinit var editTextAge : EditText
    lateinit var editHeight : EditText
    lateinit var editTextWeight : EditText
    lateinit var ref : DatabaseReference
    val currentuserid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_personalinformation, null);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("users")

        editTextNameSurname = edtNameSurname
        editTextAge = edtAge
        editHeight = edtHeight
        editTextWeight = edtWeight

        btnUpdatePersonalInformation.setOnClickListener{
            //Do something when button is clicked
            savePersonalInformation()
        }

        ref.addValueEventListener(object: ValueEventListener{
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

                            if(user!!.currentuserid == currentuserid)
                            {
                                editTextNameSurname.setText(user.namesurname)
                                editTextAge.setText(user.age.toString())
                                editHeight.setText("%.2f".format(user.height))
                                editTextWeight.setText("%.2f".format(user.weight))
                            }
                        }
                    }
                }
            }
        })
    }

    private fun savePersonalInformation()
    {
        val namesurname = editTextNameSurname.text.toString().trim()
        val age = editTextAge.text.toString().trim()
        val height = editHeight.text.toString().trim()
        val weight = editTextWeight.text.toString().trim()

        if (namesurname.isEmpty())
        {
            editTextNameSurname.error = "Please enter a name and surname"
            return
        }

        if (age.isEmpty())
        {
            editTextAge.error = "Please enter an age"
            return
        }

        if (height.isEmpty())
        {
            editHeight.error = "Please enter a height"
            return
        }

        if (weight.isEmpty())
        {
            editTextWeight.error = "Please enter a weight"
            return
        }

        val profile = "Profile"
        val user = PIData(currentuserid, namesurname, age.toInt(), height.toDouble(), weight.toDouble())
        
        ref.child(currentuserid).child(profile).setValue(user).addOnCompleteListener {
            Toast.makeText(activity, "User's information saved successfully", Toast.LENGTH_LONG).show()
        }
    }
}