package com.example.universaldietandhealthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_currentvstargetlevels.*
import kotlin.math.roundToInt

class CurrentVsTargetLevelsFragment : Fragment() {

    lateinit var editTextCHeight : EditText
    lateinit var editTextTHeight : EditText
    lateinit var editTextCWeight : EditText
    lateinit var editTextTWeight : EditText
    lateinit var editTextCCalorieIntake : EditText
    lateinit var editTextTCalorieIntake : EditText
    lateinit var ref : DatabaseReference
    val currentuserid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_currentvstargetlevels, null);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("users")

        editTextCHeight = edtCurrentHeight
        editTextTHeight = edtTargetHeight
        editTextCWeight = edtCurrentWeight
        editTextTWeight = edtTargetWeight
        editTextCCalorieIntake = edtCurrentCalorieIntake
        editTextTCalorieIntake = edtTargetCalorieIntake

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

                                if (u.child("Daily Changes").exists())
                                {
                                    val dailychanges = u.child("Daily Changes").getValue(CalendarData::class.java)

                                    if(user!!.currentuserid == currentuserid)
                                    {
                                        editTextCHeight.setText("%.2f".format(user.height))
                                        editTextTHeight.setText("%.2f".format(goals!!.heightgoal))
                                        editTextCWeight.setText("%.2f".format(user.weight))
                                        editTextTWeight.setText("%.2f".format(goals!!.weightgoal))
                                        editTextCCalorieIntake.setText(dailychanges!!.calorieintake.toString())
                                        editTextTCalorieIntake.setText(goals!!.calorieintakegoal.toString())
                                    }
                                }
                            }

                        }
                        else
                        {
                            val goals = u.child("Goals").getValue(GoalData::class.java)
                            val dailychanges = u.child("Daily Changes").getValue(CalendarData::class.java)

                            editTextTHeight.setText("%.2f".format(goals!!.heightgoal))
                            editTextTWeight.setText("%.2f".format(goals!!.weightgoal))
                            editTextCCalorieIntake.setText(dailychanges!!.calorieintake.toString())
                            editTextTCalorieIntake.setText(goals!!.calorieintakegoal.toString())
                        }
                    }
                }
            }
        })
    }
}