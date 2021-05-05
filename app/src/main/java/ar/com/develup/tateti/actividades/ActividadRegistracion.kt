package ar.com.develup.tateti.actividades

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.com.develup.tateti.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.actividad_registracion.*

/**
 * Created by maribelmai on 26/3/17.
 */
class ActividadRegistracion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_registracion)
        registrar.setOnClickListener { registrarse() }
    }

    private val registracionCompletaListener: OnCompleteListener<AuthResult?> = OnCompleteListener { task ->
        if (task.isSuccessful) {
            Snackbar.make(rootView, "Registro exitoso", Snackbar.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().currentUser.sendEmailVerification()
        } else if (task.exception is FirebaseAuthUserCollisionException) {
            Snackbar.make(rootView, "El usuario ya existe", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(rootView, "El registro fallo: " + task.exception, Snackbar.LENGTH_LONG).show()
        }
    }


    fun registrarse() {
        val passwordIngresada = password.text.toString()
        val confirmarPasswordIngresada = confirmarPassword.text.toString()
        if (email.text.toString().isEmpty()) {
            Snackbar.make(rootView, "Email requerido", Snackbar.LENGTH_SHORT).show()
        } else if (passwordIngresada == confirmarPasswordIngresada) {
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.text.toString(), passwordIngresada)
                    .addOnCompleteListener(this, registracionCompletaListener)
        } else {
            Snackbar.make(rootView, "Las contraseñas no coinciden", Snackbar.LENGTH_SHORT).show()
        }
    }
}