package com.example.marcosmarques.workers.flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.marcosmarques.workers.R;
import com.example.marcosmarques.workers.control.AdapterAnuncio;
import com.example.marcosmarques.workers.model.Anuncio;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Intent intent;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private GoogleSignInClient mGoogleSignInClient;


    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager mLayoutManager = null;
    private AdapterAnuncio adapterAnuncio = null;
    private List<Anuncio> anuncios;
    private Anuncio anuncio;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Anuncios");

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //pegar referencia do objeto usuario no firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference("anuncios");

        //inicializa lista
        anuncios = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                anuncios.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    anuncio = dataSnapshot.getValue(Anuncio.class);
                    anuncios.add(anuncio);
                }
                list();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.anunciar:
                goAnunciar();
                return true;
            case R.id.meus_anuncios:
                goMeusAnuncios();
                return true;
            case R.id.sair:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        goMain();
                    }
                });
    }

    private void goMain() {
        intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goAnunciar() {
        intent = new Intent(HomeActivity.this, AnunciarActivity.class);
        startActivity(intent);
    }

    private void goMeusAnuncios() {
        intent = new Intent(HomeActivity.this, MeusAnunciosActivity.class);
        startActivity(intent);
    }

    private void list() {
        recyclerView = (RecyclerView) findViewById(R.id.list_home);
        adapterAnuncio = new AdapterAnuncio(anuncios);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterAnuncio);
    }

}
