package ar.com.develup.tateti.actividades;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import ar.com.develup.tateti.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by maribelmai on 26/3/17.
 */

public class ActividadRegistracion extends ActividadBasica{

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.confirmarPassword)
    EditText confirmarPassword;

    private OnCompleteListener<AuthResult> registracionCompletaListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {
                Snackbar.make(rootView, "Registro exitoso", Snackbar.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
            }
            else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                Snackbar.make(rootView, "El usuario ya existe", Snackbar.LENGTH_SHORT).show();
            }
            else {
                Snackbar.make(rootView, "El registro fallo: " + task.getException(), Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.actividad_registracion;
    }

    @OnClick(R.id.registrar)
    public void registrarse() {

        String passwordIngresada = password.getText().toString();
        String confirmarPasswordIngresada = confirmarPassword.getText().toString();

        if (email.getText().toString().isEmpty()) {
            Snackbar.make(rootView, "Email requerido", Snackbar.LENGTH_SHORT).show();
        }
        else if (passwordIngresada.equals(confirmarPasswordIngresada)) {

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.getText().toString(), passwordIngresada)
                    .addOnCompleteListener(this, this.registracionCompletaListener);
        }
        else {
            Snackbar.make(rootView, "Las contraseñas no coinciden", Snackbar.LENGTH_SHORT).show();
        }
    }
}
