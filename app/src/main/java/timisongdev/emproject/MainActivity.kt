package timisongdev.emproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import timisongdev.emproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // биндинги наше все, но лучше кншн юзать jetpack compose
    private lateinit var binding: ActivityMainBinding
    private var auth = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Фильтрация кириллицы
        binding.email.filters = arrayOf(InputFilter { source, start, end, _, _, _ ->
            val regex =
                Regex("^[a-zA-Z0-9.@_+\\-]*$")

            for (i in start until end) {
                if (!regex.matches(source[i].toString())) {
                    auth = false
                    return@InputFilter ""
                } else
                    auth = true
            }
            null
        })

        // Проверяем маску email поля
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.email.error = "Неверный формат Email!"
                    auth = false
                } else {
                    binding.email.error = null
                    auth = true
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}
        })

        // Кнопка входа
        binding.login.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString()
            if (!auth || (email.isEmpty() || password.isEmpty()))
                Toast.makeText(this,
                    "Проверьте написание Email или возможно вы не заполнили все поля",
                    Toast.LENGTH_SHORT).show()
            else {
                val menu = Intent(this, MenuActivity::class.java)
                startActivity(menu)
                finish()
            }
        }

        val web = Intent(Intent.ACTION_VIEW)

        // Кнопки соц сеток
        binding.vk.setOnClickListener {
            web.data = "https://vk.com".toUri()
            startActivity(web)
        }
        binding.ok.setOnClickListener {
            web.data = "https://ok.ru".toUri()
            startActivity(web)
        }
    }
}