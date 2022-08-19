package com.example.olx_clone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    }
}