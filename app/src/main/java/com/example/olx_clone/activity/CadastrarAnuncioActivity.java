package com.example.olx_clone.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.olx_clone.databinding.ActivityCadastrarAnuncioBinding;
import com.example.olx_clone.databinding.ActivityCadastroBinding;
import com.example.olx_clone.databinding.ActivityMeusAnunciosBinding;
import com.example.olx_clone.helper.Permissoes;

public class CadastrarAnuncioActivity extends AppCompatActivity {

    private ActivityCadastrarAnuncioBinding binding;

    private String[] persmissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarAnuncioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //validar permissões
        Permissoes.validarPermissoes(persmissions, this, 1);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissionResult : grantResults){
            if(permissionResult == PackageManager.PERMISSION_DENIED){
                validationPermissionAlert();
            }
        }

    }

    private void validationPermissionAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar este app é necessários que as permissões sejam aceitas.");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}