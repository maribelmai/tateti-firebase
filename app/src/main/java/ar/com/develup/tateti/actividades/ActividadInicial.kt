package ar.com.develup.tateti.actividades

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ar.com.develup.tateti.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.actividad_inicial.*
import java.util.*

class ActividadInicial : AppCompatActivity() {

    private val authenticationListener: OnCompleteListener<AuthResult?> = OnCompleteListener<AuthResult?> { task ->
        if (task.isSuccessful) {
            if (FirebaseAuth.getInstance().currentUser.isEmailVerified) {
                verPartidas()
            } else {
                FirebaseAuth.getInstance().signOut()
                Snackbar.make(rootView!!, "Verifica tu email para continuar", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            if (task.exception is FirebaseAuthInvalidUserException) {
                Snackbar.make(rootView!!, "El usuario no existe", Snackbar.LENGTH_SHORT).show()
            } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                Snackbar.make(rootView!!, "Credenciales inválidas", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_inicial)

        iniciarSesion.setOnClickListener { iniciarSesion() }
        registrate.setOnClickListener { registrate() }
        olvideMiContrasena.setOnClickListener { olvideMiContrasena() }

        if (FirebaseAuth.getInstance().currentUser != null) {
            verPartidas()
            finish()
        }
        actualizarRemoteConfig()
        configurarOlvideMiContrasena()
    }

    private fun verPartidas() {
        val intent = Intent(this, ActividadPartidas::class.java)
        startActivity(intent)
    }

    private fun actualizarRemoteConfig() {
        val instance = FirebaseRemoteConfig.getInstance()
        instance.fetch(5)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        //instance.activateFetched();
                        configurarOlvideMiContrasena()
                    }
                }
    }

    private fun configurarOlvideMiContrasena() {
        val instance = FirebaseRemoteConfig.getInstance()
        val defaults: MutableMap<String, Any> = HashMap()
        defaults["olvideMiContrasena"] = true
        instance.setDefaultsAsync(defaults)
        //instance.setDefaults(R.xml.firebase_config_defaults);
        val featureActivo = instance.getBoolean("olvideMiContrasena")
        val visibilidad = if (featureActivo) View.VISIBLE else View.GONE
        olvideMiContrasena!!.visibility = visibilidad
    }

    fun iniciarSesion() {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email!!.text.toString(), password!!.text.toString())
                .addOnCompleteListener(authenticationListener)
    }

    fun registrate() {
        val intent = Intent(this, ActividadRegistracion::class.java)
        startActivity(intent)
    }

    fun olvideMiContrasena() {
        if (email!!.text.toString().isEmpty()) {
            Snackbar.make(rootView!!, "Completa el email", Snackbar.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email!!.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(rootView!!, "Email enviado", Snackbar.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(rootView!!, "Error " + task.exception, Snackbar.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}