package com.example.universaldietandhealthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_goals.*
import kotlin.math.roundToInt

class GoalsFragment : Fragment() {

    lateinit var editTextTargetGoalWeight : EditText
    lateinit var editTextTargetGoalCalorieIntake : EditText
    lateinit var editTextTargetHeight : EditText
    lateinit var ref : DatabaseReference
    val currentuserid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_goals, null);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("users")

        editTextTargetGoalWeight = edtTargetGoalWeight
        editTextTargetGoalCalorieIntake = edtTargetGoalCalorieIntake
        editTextTargetHeight = edtTargetGoalHeight

        btnUpdateGoals.setOnClickListener{
            //Do something when button is clicked
            saveGoals()
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

                            if (u.child("Goals").exists())
                            {
                                val goals = u.child("Goals").getValue(GoalData::class.java)

                                if(user!!.currentuserid == currentuserid)
                                {
                                    editTextTargetGoalWeight.setText("%.2f".format(goals!!.weightgoal))
                                    editTextTargetGoalCalorieIntake.setText(goals!!.calorieintakegoal.toString())
                                    editTextTargetHeight.setText("%.2f".format(goals!!.heightgoal))
                                }
                            }
                        }
                        else
                        {
                            val goals = u.child("Goals").getValue(GoalData::class.java)

                            editTextTargetGoalWeight.setText("%.2f".format(goals!!.weightgoal))
                            editTextTargetGoalCalorieIntake.setText(goals!!.calorieintakegoal.toString())
                            editTextTargetHeight.setText("%.2f".format(goals!!.heightgoal))
                        }
                    }
                }
            }
        })
    }

    private fun saveGoals()
    {
        val weightgoal = editTextTargetGoalWeight.text.toString().trim()
        val calorieintakegoal = editTextTargetGoalCalorieIntake.text.toString().trim()
        val heightgoal = editTextTargetHeight.text.toString().trim()

        if (weightgoal.isEmpty())
        {
            editTextTargetGoalWeight.error = "Please enter a target goal for weight"
            return
        }
        if (calorieintakegoal.isEmpty())
        {
            editTextTargetGoalCalorieIntake.error = "Please enter a target goal for calorie intake"
            return
        }
        if (heightgoal.isEmpty())
        {
            editTextTargetHeight.error = "Please enter a target goal for height"
            return
        }

        val goals = "Goals"
        val usergoals = GoalData(heightgoal.toDouble(), weightgoal.toDouble(), calorieintakegoal.toDouble())

        ref.child(currentuserid).child(goals).setValue(usergoals).addOnCompleteListener {
            Toast.makeText(activity, "User's goals saved successfully", Toast.LENGTH_LONG).show()
        }
    }
}