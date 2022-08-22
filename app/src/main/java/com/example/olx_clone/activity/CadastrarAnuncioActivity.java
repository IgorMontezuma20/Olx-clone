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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.olx_clone.R;
import com.example.olx_clone.databinding.ActivityCadastrarAnuncioBinding;
import com.example.olx_clone.databinding.ActivityCadastroBinding;
import com.example.olx_clone.databinding.ActivityMeusAnunciosBinding;
import com.example.olx_clone.helper.FirebaseHelper;
import com.example.olx_clone.helper.Permissoes;
import com.example.olx_clone.model.Anuncio;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class CadastrarAnuncioActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityCadastrarAnuncioBinding binding;
    private Anuncio anuncio = new Anuncio();
    private StorageReference storage;

    private String[] persmissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();
    private List<String> listaUrlFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarAnuncioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //configurações iniciais
        storage = FirebaseHelper.getStorageReference();

        //validar permissões
        Permissoes.validarPermissoes(persmissions, this, 1);

        binding.btnCadastarAnucio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarDados(view);
            }
        });

        binding.ivAnuncio1.setOnClickListener(this);
        binding.ivAnuncio2.setOnClickListener(this);
        binding.ivAnuncio3.setOnClickListener(this);

        carregarDadosSpinner();

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

    public void salvarAnuncio(){

        for(int i = 0; i < listaFotosRecuperadas.size(); i++){
            String urlImagem = listaFotosRecuperadas.get(i);
            int tamanhoLista = listaFotosRecuperadas.size();
            salvaFotoStorage(urlImagem, tamanhoLista, i);
        }

    }


    private void salvaFotoStorage(String urlImagem, int totalFotos, int contador) {

        // Cria nó no Storage
        StorageReference imagemAnuncio = storage
                .child("imagens")
                .child("anuncios")
                .child(anuncio.getIdAnuncio())
                .child("imagem" + contador);

        //Upload do arquivo
        final StorageReference imagemRef = imagemAnuncio.child(listaFotosRecuperadas+".jpeg");

        UploadTask uploadTask = imagemRef.putFile(Uri.parse(urlImagem));
        uploadTask.addOnSuccessListener(CadastrarAnuncioActivity.this,
                (OnSuccessListener)(uploadTask),
                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        Uri url = task.getResult();
                        String urlConvertida = url.toString();

                        listaUrlFotos.add(urlConvertida);

                        if(totalFotos == listaUrlFotos.size()){

                            anuncio.setFotos(listaUrlFotos);
                            anuncio.salvar();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                exibirMsgErro("Erro ao salvar imagem");
            }
        });

    }


    private Anuncio configurarAnuncio(){

        String estado = binding.spinnerEstado.getSelectedItem().toString();
        String categoria = binding.spinnerCategoria.getSelectedItem().toString();
        String titulo = binding.edtTitulo.getText().toString();
        String valor = String.valueOf(binding.edtValor.getRawValue());
        String telefone = binding.edtTelefone.getText().toString();
        String descricao = binding.edtDescricao.getText().toString();

        Anuncio anuncio = new Anuncio();
        anuncio.setEstado(estado);
        anuncio.setCategoria(categoria);
        anuncio.setTitulo(titulo);
        anuncio.setValor(valor);
        anuncio.setTelefone(telefone);
        anuncio.setDescricao(descricao);

        return  anuncio;

    }

    public void validarDados(View view){

       anuncio = configurarAnuncio();

        if(listaFotosRecuperadas.size() != 0){
            if(!anuncio.getEstado().isEmpty()){
                if(!anuncio.getCategoria().isEmpty()){
                    if(!anuncio.getTitulo().isEmpty()){
                            if(!anuncio.getValor().isEmpty() && !anuncio.getValor().equals("0")){
                                if(!anuncio.getTelefone().isEmpty()){
                                    if(!anuncio.getDescricao().isEmpty()){
                                        salvarAnuncio();
                                    }else{
                                        exibirMsgErro("Preencha o campo descrição!.");
                                    }
                                }else{
                                    exibirMsgErro("Preencha o campo telefone!.");
                                }
                            }else{
                                exibirMsgErro("Preencha o campo valor!.");
                            }

                    }else{
                        exibirMsgErro("Preenca o campo tíutlo!");
                    }
                }else{
                    exibirMsgErro("Preencha o campo categoria!.");
                }
            }else{
                exibirMsgErro("Preencha o campo estado!.");
            }
        }else{
            exibirMsgErro("Selecione ao menos uma foto!");
        }

    }

    private void exibirMsgErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
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

    private void carregarDadosSpinner(){
        //Configuração spinner Estados
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, estados);
        adapterEstados.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEstado.setAdapter(adapterEstados);

        String[] categorias = getResources().getStringArray(R.array.categorias);
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categorias);
        adapterCategoria.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoria.setAdapter(adapterCategoria);
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