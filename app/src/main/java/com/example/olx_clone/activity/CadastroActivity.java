package com.example.olx_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.olx_clone.databinding.ActivityCadastroBinding;
import com.example.olx_clone.helper.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseHelper.getAuth();

        binding.btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.edtUserEmail.getText().toString();
                String password = binding.edtUserPassword.getText().toString();

                if(!email.isEmpty()){

                    if(!password.isEmpty()){

                        if(binding.swAcesso.isChecked()){

                            auth.createUserWithEmailAndPassword(
                                    email, password
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CadastroActivity.this,
                                                "Cadastro efetuado!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        String erroExcecao = "";

                                        try {
                                            throw task.getException();
                                        }catch( FirebaseAuthWeakPasswordException e){
                                            erroExcecao = "Digite uma senha mais forte!";
                                        }catch ( FirebaseAuthInvalidCredentialsException e ){
                                            erroExcecao = "Por favor, digite um email válido.";
                                        }catch (FirebaseAuthUserCollisionException e){
                                            erroExcecao = "Esta conta já é registrada";
                                        }catch (Exception e){
                                            erroExcecao = "ao cadastrar usuário" + e.getMessage();
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(CadastroActivity.this, "ERRO: "+erroExcecao,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            //Login

                            auth.signInWithEmailAndPassword(
                                    email, password
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(
                                                CadastroActivity.this, "Logado com sucesso!",
                                                Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(),
                                                AnunciosActivity.class));

                                    }else{

                                        Toast.makeText(CadastroActivity.this,
                                                "Erro ao fazer login: " + task.getException(),
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }

                    }else{
                        Toast.makeText(CadastroActivity.this, "Por favor, informe sua senha",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(CadastroActivity.this, "Por favor, informe seu E-mail",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}