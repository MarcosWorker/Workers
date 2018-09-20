package com.example.marcosmarques.workers.flow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.marcosmarques.workers.R;
import com.example.marcosmarques.workers.control.AdapterAnuncio;
import com.example.marcosmarques.workers.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MeusAnunciosActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private List<Anuncio> anuncios;
    private Anuncio anuncio;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);
        setTitle("Meus Anuncios");

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("anuncios");

        anuncios = new ArrayList<>();
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
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    if (user.getUid().equals(anuncio.getId())) {
                        anuncios.add(anuncio);
                    }
                }
                list();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MeusAnunciosActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void list() {
        RecyclerView recyclerView = findViewById(R.id.list_meus_anuncios);
        AdapterAnuncio adapterAnuncio = new AdapterAnuncio(anuncios, getClass().getSimpleName(), MeusAnunciosActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapterAnuncio);
    }

}
