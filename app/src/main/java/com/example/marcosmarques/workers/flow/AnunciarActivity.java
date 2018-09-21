package com.example.marcosmarques.workers.flow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.marcosmarques.workers.R;
import com.example.marcosmarques.workers.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AnunciarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText servico;
    private EditText descricao;
    private EditText telefone;
    private EditText local;
    private String tipoSelecionado;
    private DatabaseReference mDatabase;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunciar);
        setTitle("Novo Anuncio");

        servico = findViewById(R.id.servico);
        descricao = findViewById(R.id.descricao);
        telefone = findViewById(R.id.telefone);
        local = findViewById(R.id.local);
        Spinner tipo = findViewById(R.id.spinner_tipo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipo.setAdapter(adapter);
        tipo.setOnItemSelectedListener(this);

        Button salvar = findViewById(R.id.salvar_anuncio);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarAnuncio();
            }
        });

        //pegar referencia do objeto usuario no firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference("anuncios");

        // [START initialize_auth]
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        user = mAuth.getCurrentUser();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tipoSelecionado = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void salvarAnuncio() {

        //validação para salvar
        if (servico.getText().toString().isEmpty() ||
                descricao.getText().toString().isEmpty() ||
                telefone.getText().toString().isEmpty() ||
                local.getText().toString().isEmpty()) {
            Toast.makeText(this, "Algum campo está vazio!", Toast.LENGTH_SHORT).show();
        } else {
            String id = mDatabase.push().getKey();

            Anuncio anuncio = new Anuncio();
            anuncio.setDescricao(descricao.getText().toString());
            anuncio.setLocal(local.getText().toString());
            anuncio.setServico(servico.getText().toString());
            anuncio.setTelefone(telefone.getText().toString());
            anuncio.setTipo(tipoSelecionado);
            anuncio.setUsuario(user.getDisplayName());
            anuncio.setId(user.getUid());
            anuncio.setuId(id);
            anuncio.setImage(Objects.requireNonNull(user.getPhotoUrl()).toString());

            Date data = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dformatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dformatador.format(data);

            anuncio.setTimestamp(timestamp);

            mDatabase.child(id).setValue(anuncio);

            Toast.makeText(this, "Anuncio salvo", Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}
