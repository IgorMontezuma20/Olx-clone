package com.example.olx_clone.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.olx_clone.databinding.ActivityCadastrarAnuncioBinding;
import com.example.olx_clone.databinding.ActivityCadastroBinding;
import com.example.olx_clone.databinding.ActivityMeusAnunciosBinding;

public class CadastrarAnuncioActivity extends AppCompatActivity {

    private ActivityCadastrarAnuncioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarAnuncioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCadastarAnucio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarAnuncio(view);
            }
        });

    }

    public void salvarAnuncio(View view){

        String telefone = binding.edtTelefone.getText().toString();
        Log.v("salvar", "salvarAnuncio: "+telefone);
    }
}