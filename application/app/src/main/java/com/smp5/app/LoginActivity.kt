package com.smp5.app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.smp5.app.AuthPrefs
import com.smp5.app.MainActivity
import com.smp5.app.databinding.ActivityLoginBinding
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authPrefs: AuthPrefs
    private val api by lazy { APIRetrofit(this).endpoint }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authPrefs = AuthPrefs(this)

        // Check if token exists and navigate to MainActivity if it does
        val savedToken = authPrefs.getToken()
        if (savedToken != null) {
            authPrefs.saveToken(savedToken)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val name = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            api.login(name, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val token = response.body()?.token
                        if (token != null) {
                            // Save token to SharedPreferences
                            authPrefs.saveToken(token)
                            // Set token for API calls
                            // Navigate to MainActivity
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}