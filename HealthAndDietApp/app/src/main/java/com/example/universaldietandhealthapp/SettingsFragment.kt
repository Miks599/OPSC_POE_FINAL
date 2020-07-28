package com.example.universaldietandhealthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var ref : DatabaseReference
    val currentuserid = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_settings, null);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var imperialmetric = ""

        ref = FirebaseDatabase.getInstance().getReference("users")

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
                                        rbImperialSystem.setOnClickListener{
                                            //Do something when button is clicked
                                            if (rbImperialSystem.isChecked == true)
                                            {
                                                imperialmetric = "imperial"
                                                val lol4 = SettingsData(imperialmetric)
                                                ref.child(currentuserid).child("Settings").setValue(lol4)
                                            }

                                            user.height = user.height / 0.0254
                                            user.weight = user.weight / 0.45359237
                                            goals!!.heightgoal = goals!!.heightgoal / 0.0254
                                            goals!!.weightgoal = goals!!.weightgoal / 0.45359237
                                            dailychanges!!.dailyweightchange = dailychanges!!.dailyweightchange / 0.45359237

                                            val lol1 = PIData(currentuserid, user.namesurname, user.age, user.height, user.weight)
                                            ref.child(currentuserid).child("Profile").setValue(lol1)

                                            val lol2 = GoalData(goals.heightgoal, goals.weightgoal, goals.calorieintakegoal)
                                            ref.child(currentuserid).child("Goals").setValue(lol2)

                                            val lol3 = CalendarData(dailychanges.date, dailychanges.dailyweightchange, dailychanges.waterintake, dailychanges.calorieintake)
                                            ref.child(currentuserid).child("Daily Changes").setValue(lol3)
                                        }

                                        rbMetricSystem.setOnClickListener{
                                            //Do something when button is clicked
                                            if (rbMetricSystem.isChecked == true)
                                            {
                                                imperialmetric = "metric"
                                                val lol4 = SettingsData(imperialmetric)
                                                ref.child(currentuserid).child("Settings").setValue(lol4)
                                            }

                                            user.height = user.height * 0.0254
                                            user.weight = user.weight * 0.45359237
                                            goals!!.heightgoal = goals!!.heightgoal * 0.0254
                                            goals!!.weightgoal = goals!!.weightgoal * 0.45359237
                                            dailychanges!!.dailyweightchange = dailychanges!!.dailyweightchange * 0.45359237

                                            val lol1 = PIData(currentuserid, user.namesurname, user.age, user.height, user.weight)
                                            ref.child(currentuserid).child("Profile").setValue(lol1)

                                            val lol2 = GoalData(goals.heightgoal, goals.weightgoal, goals.calorieintakegoal)
                                            ref.child(currentuserid).child("Goals").setValue(lol2)

                                            val lol3 = CalendarData(dailychanges.date, dailychanges.dailyweightchange, dailychanges.waterintake, dailychanges.calorieintake)
                                            ref.child(currentuserid).child("Daily Changes").setValue(lol3)
                                        }
                                    }
                                }
                            }
                            if (u.child("Settings").exists())
                            {
                                val settingz = u.child("Settings").getValue(SettingsData::class.java)

                                if (settingz!!.imperialmetric == "imperial")
                                {
                                    rbMetricSystem.isChecked = false
                                    rbImperialSystem.isChecked = true
                                }
                                if (settingz.imperialmetric == "metric")
                                {
                                    rbImperialSystem.isChecked = false
                                    rbMetricSystem.isChecked = true
                                }
                            }
                        }
                        else
                        {
                            val goals = u.child("Goals").getValue(GoalData::class.java)
                            val dailychanges = u.child("Daily Changes").getValue(CalendarData::class.java)

                            rbImperialSystem.setOnClickListener{
                                //Do something when button is clicked
                                if (rbImperialSystem.isChecked == true)
                                {
                                    imperialmetric = "imperial"
                                    val lol4 = SettingsData(imperialmetric)
                                    ref.child(currentuserid).child("Settings").setValue(lol4)
                                }

                                goals!!.heightgoal = goals!!.heightgoal / 0.0254
                                goals!!.weightgoal = goals!!.weightgoal / 0.45359237
                                dailychanges!!.dailyweightchange = dailychanges!!.dailyweightchange / 0.45359237

                                val lol2 = GoalData(goals.heightgoal, goals.weightgoal, goals.calorieintakegoal)
                                ref.child(currentuserid).child("Goals").setValue(lol2)

                                val lol3 = CalendarData(dailychanges.date, dailychanges.dailyweightchange, dailychanges.waterintake, dailychanges.calorieintake)
                                ref.child(currentuserid).child("Daily Changes").setValue(lol3)
                            }

                            rbMetricSystem.setOnClickListener{
                                //Do something when button is clicked
                                if (rbMetricSystem.isChecked == true)
                                {
                                    imperialmetric = "metric"
                                    val lol4 = SettingsData(imperialmetric)
                                    ref.child(currentuserid).child("Settings").setValue(lol4)
                                }

                                goals!!.heightgoal = goals!!.heightgoal * 0.0254
                                goals!!.weightgoal = goals!!.weightgoal * 0.45359237
                                dailychanges!!.dailyweightchange = dailychanges!!.dailyweightchange * 0.45359237

                                val lol2 = GoalData(goals.heightgoal, goals.weightgoal, goals.calorieintakegoal)
                                ref.child(currentuserid).child("Goals").setValue(lol2)

                                val lol3 = CalendarData(dailychanges.date, dailychanges.dailyweightchange, dailychanges.waterintake, dailychanges.calorieintake)
                                ref.child(currentuserid).child("Daily Changes").setValue(lol3)
                            }
                        }
                    }
                }
            }
        })
    }
}