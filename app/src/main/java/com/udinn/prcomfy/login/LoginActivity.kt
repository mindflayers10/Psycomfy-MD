package com.udinn.prcomfy.login

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.udinn.prcomfy.HomeFragment
import com.udinn.prcomfy.MainActivity
import com.udinn.prcomfy.R
import com.udinn.prcomfy.api.RetrofitInstance
import com.udinn.prcomfy.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var account : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        account = getSharedPreferences("login_session", MODE_PRIVATE)
        if (account.getString("email", null)!=null){
            if (account.getString("token",null)!= null)
                startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        // Configure Google Sign In
        binding.crtAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.cardLogin.setOnClickListener {
            getData()
        }
    }

    //Get Data Login
    private fun getData() {
        val email = binding!!.edEmail.text.toString()
        val password = binding!!.edPassword.text.toString()
        val api = RetrofitInstance.getApiService()
        api.userLogin(email, password).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                getSharedPreferences("login_session", MODE_PRIVATE)
                    .edit()
                    .putString("email", response.body()?.user?.email)
                    .putString("token", response.body()?.user?.token)
                    .apply()

                val responseBody = response.body()
                if (responseBody!=null && !responseBody.error){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    binding.loadingScreen.visibility=View.VISIBLE
                    finish()
                    Toast.makeText(this@LoginActivity,"Berhasil Login", Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(this@LoginActivity,"gagal Login", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"gagal retrofit", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}