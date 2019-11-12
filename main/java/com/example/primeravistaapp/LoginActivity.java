package com.example.primeravistaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String correo = "";
    private String contraseña = "";
    private ViewGroup contenedor;
    private EditText etCorreo, etContraseña;
    private Button botonResetPassword;
    private static final AtomicInteger count = new AtomicInteger(0);
    private double idAdmin;

    private ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login();
        /*botonResetPassword = (Button) findViewById(R.id.btnResetPassword);
        botonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, cambiarContraseña.class));
            }


        });*/
        db.collection("Usuarios").document("Admin").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    idAdmin = task.getResult().getDouble("IDdefecto");


                }
            }
        });


    }

    private void login() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            //FirebaseUser usuario = firebaseAuth.getCurrentUser();


            //Toast.makeText(this, "inicia sesión: " + usuario.getDisplayName() + " - " + usuario.getEmail() + " - " + usuario.getProviders().get(0), Toast.LENGTH_LONG).show();

            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);


        } else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).build(), new AuthUI.IdpConfig.GoogleBuilder().build())).build()
                    //.setIsSmartLockEnabled(false)
                    , RC_SIGN_IN);


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                double idNueva = idAdmin+1;
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                Map<String, Object> datos = new HashMap<>();
                datos.put("Nombre", usuario.getDisplayName());
                //datos.put("ID",1);
                datos.put("ID", idNueva);
                db.collection("Usuarios").document(usuario.getDisplayName()).set(datos);
                //login();
                Map<String, Object> datosadmin = new HashMap<>();
                datosadmin.put("IDdefecto", idNueva);
                db.collection("Usuarios").document("Admin").set(datosadmin);

                if (!usuario.isEmailVerified()) {
                    Toast.makeText(this, "Verifica el correo: " + usuario.getEmail(), Toast.LENGTH_LONG).show();
                    usuario.sendEmailVerification();

                } else {
                    login();
                    //finish();
                }


            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Sin conexión a Internet",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error desconocido",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    public void verificar() {

    }

}


