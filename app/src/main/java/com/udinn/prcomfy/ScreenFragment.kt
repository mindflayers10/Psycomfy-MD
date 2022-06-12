package com.udinn.prcomfy

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.udinn.prcomfy.databinding.FragmentScreenBinding
import com.udinn.prcomfy.login.LoginActivity
import com.udinn.prcomfy.utils.ApiCallbackString
import com.udinn.prcomfy.utils.ListViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.format
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ScreenFragment : Fragment(R.layout.fragment_screen) {
    //UI
    private var _binding: FragmentScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    private lateinit var navigation : NavController

    //variabel
    private var isRecording = false
    private var mediaRecorder: MediaRecorder?= null
    private lateinit var record_file_name : String
    private lateinit var dir : File
    private val viewModel by viewModels<ListViewModel>()

    //Permissions
    private val permissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.RECORD_AUDIO
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScreenBinding.bind(view)
        navigation = Navigation.findNavController(binding.root)

        binding.listMenu.setOnClickListener {
            if(isRecording)
            {
                check(false)
            }
            else
            {
                navigation.navigate(R.id.listFragment)
            }

        }

        binding.voice.setOnClickListener {
            if (isRecording){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    check(true)
                }
                Toast.makeText(activity,"Recording Ended", Toast.LENGTH_SHORT).show()
            }else{
                if(chekPermissions())
                {

                    startRecording()
                    Toast.makeText(activity,"Recording Started",Toast.LENGTH_SHORT).show()
                    isRecording=true
                }
            }
        }

    }



    private fun startRecording() {

        binding.recordTimer.base = SystemClock.elapsedRealtime()
        binding.recordTimer.start()

        //Getting Record Path And Mediaplayer Works
        val recordPath = activity?.getExternalFilesDir("/")!!.absolutePath
        val format = SimpleDateFormat("yyyyMMddhhss", Locale.CANADA)
        val now = Date()
//        record_file_name = "Recoding_"+format.format(now)+".wav"
        record_file_name = "_"+ format.format(now)+".wav"
        binding.recorAudioName.text =  record_file_name
        mediaRecorder= MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setOutputFile(recordPath + "/" + record_file_name)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder!!.prepare()
        mediaRecorder!!.start()
    }

    private fun chekPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),permissions,5000)

        }

        return false
    }
    private fun stopRecording()
    {

        binding.recordTimer.stop()
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
        mediaRecorder=null
        isRecording=false
        binding.recorAudioName.text = record_file_name

    }


    @SuppressLint("UseRequireInsteadOfGet")
    fun check(flag : Boolean)
    {
        //Chronometer UI
        var timeWhenStopped: Long = 0
        timeWhenStopped = binding.recordTimer.getBase() - SystemClock.elapsedRealtime()
        binding.recordTimer.stop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder!!.pause()
        }

        //Alert Dialog
        alertDialog = MaterialAlertDialogBuilder(activity!!)
            .setCancelable(false)
            .setMessage("Do you want to stop the recording?")
            .setTitle("Hey!!")
            .setPositiveButton("Stop", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    if(flag)
                    {
                        binding.recordTimer.stop()
                        stopRecording()
                    }
                    else
                    {
                        binding.recordTimer.stop()
                        stopRecording()
                        navigation.navigate(R.id.listFragment)
                    }
                    alertDialog.dismiss()

                }

            }).setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    binding.recordTimer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped)
                    binding.recordTimer.start()
                    alertDialog.dismiss()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mediaRecorder!!.resume()
                    }
                }

            }).create()



        alertDialog.show()



    }

}