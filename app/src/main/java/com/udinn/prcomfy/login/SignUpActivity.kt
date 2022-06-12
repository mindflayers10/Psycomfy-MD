package com.udinn.prcomfy.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.udinn.prcomfy.MainActivity
import com.udinn.prcomfy.R
import com.udinn.prcomfy.api.RetrofitInstance
import com.udinn.prcomfy.databinding.ActivitySignUpBinding
import com.udinn.prcomfy.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnSignUp.setOnClickListener {
            getData()
        }
        binding.haveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun getData() {
        val email = binding.edEmail.text.toString()
        val password = binding.edpassword.text.toString().trim()
        val confirmPass = binding.edConPassword.text.toString()
        showLoading(true)
        val api = RetrofitInstance.getApiService()
        api.userRegister(email,password,confirmPass).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val responseBody = response.body()
                if (responseBody!=null && !responseBody.error){
                    val view = View.inflate(this@SignUpActivity, R.layout.dialog_view, null)
                    val builder = AlertDialog.Builder(this@SignUpActivity)
                    builder.setView(view)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    val btnLogin = dialog.findViewById<CardView>(R.id.cobaLogin)
                    showLoading(true)
                    btnLogin?.setOnClickListener {
                        Intent(this@SignUpActivity, LoginActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                    Toast.makeText(this@SignUpActivity, "Berhasil register",Toast.LENGTH_SHORT).show()
                }else{
                    val view = View.inflate(this@SignUpActivity, R.layout.dialog_fail, null)
                    val builder = AlertDialog.Builder(this@SignUpActivity)
                    builder.setView(view)
                    val dialog = builder.create()
                    dialog.show()
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    val btnLogin = dialog.findViewById<CardView>(R.id.tryAgain)
                    showLoading(false)
                    btnLogin?.setOnClickListener {
                        dialog.dismiss()
                    }
                    Toast.makeText(this@SignUpActivity, "Gagal register",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Gagal retrofit",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingScreen.visibility = View.VISIBLE
        } else {
            binding.loadingScreen.visibility = View.GONE
        }
    }
}