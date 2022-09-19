package com.example.testapp1

import android.app.*
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.testapp1.databinding.FragmentTaskBinding
import java.text.SimpleDateFormat
import java.util.*


class Task : Fragment() {

    var textview_date: TextView? = null
    var textview_time: TextView? = null
    var cal = Calendar.getInstance()
    var _binding : FragmentTaskBinding? = null
    private val binding get() = _binding!!

    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent

    var Year1: Int? = null
    var Month1: Int? = null
    var Date1: Int? = null
    var Hour1: Int? = null
    var Minute1: Int? = null
    public  var uri:Uri ?= null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        textview_date = binding.date
        textview_time = binding.time

        textview_date!!.text = "--/--/----"

        getDate()

        getTime(textview_time!!, requireContext())

        binding.relative1.setOnClickListener {
            pickAudio()
        }
        binding.button.setOnClickListener {
            if (textview_date!=null  && textview_time!= null && uri!=null ) {

                setAlarm()

            }
        }

        createNotificationChannel()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAlarm(){

        val cal1 = Calendar.getInstance()

        cal1.timeInMillis = System.currentTimeMillis()
        cal1.clear()
        if (Year1 != null && Month1!=null && Date1!=null && Hour1!=null && Minute1!=null ) {
            cal1.set(Year1!!, Month1!!, Date1!!, Hour1!!, Minute1!!)
        }
        textview_date!!.text

        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),MyReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,PendingIntent.FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal1.timeInMillis,pendingIntent)

        Toast.makeText(requireContext(),"success",Toast.LENGTH_SHORT).show()
    }



    fun getDate(){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                textview_date!!.text = SimpleDateFormat("MM/dd/yyyy", Locale.US).format(cal.getTime())
                Year1 = year
                Month1 = monthOfYear
                Date1 = dayOfMonth

            }
        }
        binding.date.setOnClickListener {
            DatePickerDialog(requireContext(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun getTime(textView: TextView, context: Context){

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("HH:mm").format(cal.time)
            Hour1 = hour
            Minute1 = minute

        }

        textView.setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }


    fun pickAudio(){
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Audio "), 1011)
    }


    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
        {
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            /*val name = "Task Manager"
            val discription = "Channel For Task Manager"

            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel1 = NotificationChannel("123",name,importance)
            channel1.description = discription

            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val channel = NotificationChannel(
                "TaskManagement",
                requireContext().getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )*/

            val importance = NotificationManager.IMPORTANCE_HIGH
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()


            val channel = NotificationChannel("TaskManagement", getString(R.string.app_name), importance).apply {
                description = getString(R.string.app_name)
                setSound(alarmSound, audioAttributes) // Give Null if you want silent notification
                // setSound(soundUri, audioAttributes)
            }


            //channel.enableLights(true)
            //channel.enableVibration(true)
            //channel.setSound(alarmSound, attributes)


            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( resultCode == RESULT_OK && requestCode == 1011 )
        {
            uri = data?.data!!

        }
        else{


        }
    }

}