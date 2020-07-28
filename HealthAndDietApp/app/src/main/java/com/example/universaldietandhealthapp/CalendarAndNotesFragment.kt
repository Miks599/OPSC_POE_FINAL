package com.example.universaldietandhealthapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_calendarandnotes.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CalendarAndNotesFragment : Fragment() {

    lateinit var editTextDailyWeightChanges : EditText
    lateinit var editTextWaterIntake : EditText
    lateinit var editTextCalorieIntake : EditText
    lateinit var ref : DatabaseReference
    val currentuserid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_calendarandnotes, null);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("users")

        editTextDailyWeightChanges = edtDailyWeightChanges
        editTextWaterIntake = edtWaterIntake
        editTextCalorieIntake = edtTotalCalorieIntake

        btnUpdateDailyWeightChanges.setOnClickListener{
            //Do something when button is clicked
            saveChanges()
        }

        btnUpdateWaterIntake.setOnClickListener{
            //Do something when button is clicked
            saveChanges()
        }

        btnUpdateTotalCalorieIntake.setOnClickListener{
            //Do something when button is clicked
            saveChanges()
        }

        btnAddMeal.setOnClickListener{
            //Do something when button is clicked
            val intent = Intent(activity, MealActivity::class.java)
            startActivity(intent)
        }

        btnAddNotes.setOnClickListener{
            //Do something when button is clicked
            val intent = Intent(activity, NotesActivity::class.java)
            startActivity(intent)
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

                            if (u.child("Daily Changes").exists())
                            {
                                val changes = u.child("Daily Changes").getValue(CalendarData::class.java)

                                if(user!!.currentuserid == currentuserid)
                                {
                                    editTextDailyWeightChanges.setText("%.2f".format(changes!!.dailyweightchange))
                                    editTextWaterIntake.setText(changes!!.waterintake)
                                    editTextCalorieIntake.setText(changes!!.calorieintake.toString())
                                }
                            }
                        }
                        else
                        {
                            val changes = u.child("Daily Changes").getValue(CalendarData::class.java)

                            editTextDailyWeightChanges.setText("%.2f".format(changes!!.dailyweightchange))
                            editTextWaterIntake.setText(changes!!.waterintake)
                            editTextCalorieIntake.setText(changes!!.calorieintake.toString())
                        }
                    }
                }
            }
        })

        /*calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val currentDate = dayOfMonth.toString()
        }*/
    }

    private fun saveChanges()
    {
        val dailyweightchange = editTextDailyWeightChanges.text.toString().trim()
        val waterintake = editTextWaterIntake.text.toString().trim()
        val calorieintake = editTextCalorieIntake.text.toString().trim()

        if (dailyweightchange.isEmpty())
        {
            editTextDailyWeightChanges.error = "Please enter a daily weight change"
            return
        }
        if (waterintake.isEmpty())
        {
            editTextWaterIntake.error = "Please enter a water intake"
            return
        }
        if (calorieintake.isEmpty())
        {
            editTextCalorieIntake.error = "Please enter a calorie intake"
            return
        }

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        val changes = "Daily Changes"
        val dailychanges = CalendarData(currentDate.toString(), dailyweightchange.toDouble(), waterintake, calorieintake.toDouble())

        ref.child(currentuserid).child(changes).setValue(dailychanges).addOnCompleteListener {
            Toast.makeText(activity, "Daily changes saved successfully", Toast.LENGTH_LONG).show()
        }
    }
}