package com.example.myloginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myloginapp.api.LoginRequest
import com.example.myloginapp.api.LoginResponse
import com.example.myloginapp.api.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameInput = findViewById<EditText>(R.id.editTextUsername)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Agar username yoki password bo'sh bo'lsa
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Login va parolni kiriting!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // LoginRequest ob'ekti yaratish
            val request = LoginRequest(username, password)

            // CoroutineScope yordamida asinxron ishlash
            lifecycleScope.launch {
                try {
                    // RetrofitClient yordamida login so'rovini yuboramiz
                    val response = RetrofitClient.instance.login(request)
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val token = loginResponse?.access // Token olish
                        val refreshToken = loginResponse?.refresh // Refresh token olish (optional)

                        if (!token.isNullOrEmpty()) {
                            Log.d("LOGIN_SUCCESS", "Token olindi: $token")
                            Toast.makeText(this@LoginActivity, "Login muvaffaqiyatli!", Toast.LENGTH_SHORT).show()

                            // Keyingi ekranga token yuborib o'tamiz
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("TOKEN", token)
                            intent.putExtra("REFRESH_TOKEN", refreshToken)
                            startActivity(intent)
                            finish() // LoginActivity'ni yopamiz
                        } else {
                            Toast.makeText(this@LoginActivity, "Token olinmadi!", Toast.LENGTH_SHORT).show()
                            Log.e("LOGIN_ERROR", "Token yo'q")
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login muvaffaqiyatsiz! Tekshirib ko'ring.", Toast.LENGTH_SHORT).show()
                        Log.e("LOGIN_ERROR", "Login xatosi: ${response.code()}")
                    }
                } catch (e: Exception) {
                    // Tarmoq xatolari bo'lsa
                    Toast.makeText(this@LoginActivity, "Tarmoq xatosi: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("LOGIN_FAILURE", "Tarmoq xatosi: ${e.message}")
                }
            }
        }
    }
}
