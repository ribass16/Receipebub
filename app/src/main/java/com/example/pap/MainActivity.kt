package com.example.pap

import ListaFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.pap.databinding.ActivityMainBinding
import com.example.pap.views.Home

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        replaceFragment(Home())


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                //R.id.home -> replaceFragment(Home())
                R.id.add -> {
                    launcher.launch(
                        Intent(
                            applicationContext,
                            TelaIngredientes::class.java
                        )
                    )
                    replaceFragment(Home())
                }
                //startActivity(Intent(this, TelaIngredientes::class.java))
                R.id.comu -> replaceFragment(ListaFragment())
                R.id.perfil -> replaceFragment(Perfil())
                else -> {
                    replaceFragment(Home())
                }
            }
            true
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                replaceFragment(Home())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }


}
