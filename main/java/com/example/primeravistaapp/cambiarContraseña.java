package com.example.primeravistaapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class cambiarContraseña extends AppCompatActivity {

    private EditText editTextMail;
    private Button botonresetPassword;
    private String email = "";
    private FirebaseAuth Auth;
    private ProgressDialog dialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_contrasenya);
        dialog = new ProgressDialog(this);
        Auth = FirebaseAuth.getInstance();
        editTextMail = (EditText) findViewById(R.id.editTextCambiarContra);
        botonresetPassword = (Button) findViewById(R.id.botonCambiarPassword);
        botonresetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editTextMail.getText().toString();
                if (!email.isEmpty()) {
                    dialog.setMessage("Espere un momento ...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    resetPassword();
                } else {
                    Toast.makeText(cambiarContraseña.this, "Debe ingresar un email", Toast.LENGTH_SHORT).show();
                }
            }


        });

    }

    private void resetPassword() {
        Auth.setLanguageCode("es");
        Auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(cambiarContraseña.this, "Se ha enviado un correo para reestablecer contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(cambiarContraseña.this, "No se pudo enviar el correo para cambiar de contraseña", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
}
