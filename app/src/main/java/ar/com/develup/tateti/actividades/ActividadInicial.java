package ar.com.develup.tateti.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import ar.com.develup.tateti.R;
import butterknife.BindView;
import butterknife.OnClick;

public class ActividadInicial extends ActividadBasica {

    private String TAG = ActividadInicial.class.getSimpleName();

    @BindView(R.id.rootView)
    View rootView;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    private OnCompleteListener<AuthResult> authenticationListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {

                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                    crearPartida();
                }
                else {
                    FirebaseAuth.getInstance().signOut();
                    Snackbar.make(rootView, "Verifica tu email para continuar", Snackbar.LENGTH_SHORT).show();
                }
            }
            else {

                if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                    Snackbar.make(rootView, "El usuario no existe", Snackbar.LENGTH_SHORT).show();
                }
                else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(rootView, "Credenciales inválidas", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            crearPartida();
            finish();
        }
    }

    private void crearPartida() {

        Intent intent = new Intent(this, ActividadPartida.class);
        startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.actividad_inicial;
    }

    @OnClick(R.id.iniciarSesion)
    public void iniciarSesion() {

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this.authenticationListener);
    }

    @OnClick(R.id.registrate)
    public void registrate() {

        Intent intent = new Intent(this, ActividadRegistracion.class);
        startActivity(intent);
    }

    @OnClick(R.id.olvideMiContrasena)
    public void olvideMiContrasena() {

        if (email.getText().toString().isEmpty()) {
            Snackbar.make(rootView, "Completa el email", Snackbar.LENGTH_SHORT).show();
        }
        else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Snackbar.make(rootView, "Email enviado", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        Snackbar.make(rootView, "Error " + task.getException(), Snackbar.LENGTH_SHORT).show();
                    }
                }
            });;
        }
    }
}
