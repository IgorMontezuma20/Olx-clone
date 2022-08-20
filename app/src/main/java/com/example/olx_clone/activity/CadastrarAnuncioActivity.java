package com.example.olx_clone.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.olx_clone.R;
import com.example.olx_clone.databinding.ActivityCadastrarAnuncioBinding;
import com.example.olx_clone.databinding.ActivityCadastroBinding;
import com.example.olx_clone.databinding.ActivityMeusAnunciosBinding;
import com.example.olx_clone.helper.Permissoes;

import java.util.ArrayList;
import java.util.List;

public class CadastrarAnuncioActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityCadastrarAnuncioBinding binding;

    private String[] persmissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();

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

        binding.ivAnuncio1.setOnClickListener(this);
        binding.ivAnuncio2.setOnClickListener(this);
        binding.ivAnuncio3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ivAnuncio1 :
                selectImage(1);
                break;
            case R.id.ivAnuncio2:
                selectImage(2);
                break;
            case R.id.ivAnuncio3:
                selectImage(3);
                break;
        }

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

    public void selectImage( int requestCode){
        Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == Activity.RESULT_OK){

            //Recuperando a imagem
            Uri selectedImage =data.getData();
            String imagePath = selectedImage.toString();

            //Configura imagem no ImageView
            if( requestCode == 1){
                binding.ivAnuncio1.setImageURI(selectedImage);
            }else if(requestCode == 2){
                binding.ivAnuncio2.setImageURI(selectedImage);
            }else if(requestCode == 3){
                binding.ivAnuncio3.setImageURI(selectedImage);
            }
            listaFotosRecuperadas.add(imagePath);
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