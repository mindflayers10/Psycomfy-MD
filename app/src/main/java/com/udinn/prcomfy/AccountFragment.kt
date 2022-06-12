package com.udinn.prcomfy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.udinn.prcomfy.databinding.FragmentAccountBinding
import com.udinn.prcomfy.databinding.FragmentScreenBinding
import com.udinn.prcomfy.login.LoginActivity


class AccountFragment : Fragment(R.layout.fragment_account) {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var account : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        account = activity?.getSharedPreferences("login_session", AppCompatActivity.MODE_PRIVATE)!!

        binding?.textName?.text = account.getString("email", null)
        binding?.textToken?.text = account.getString("token", null)

        binding.btnLogOut.setOnClickListener {
            account.edit().clear().commit()

            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

    }

}