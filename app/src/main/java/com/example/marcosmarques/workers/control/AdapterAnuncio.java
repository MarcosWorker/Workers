package com.example.marcosmarques.workers.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcosmarques.workers.R;
import com.example.marcosmarques.workers.model.Anuncio;
import com.example.marcosmarques.workers.util.PicassoCircleTransformation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnuncio extends RecyclerView.Adapter<AdapterAnuncio.ViewHolder> {

    private List<Anuncio> anuncios;
    private String nameActivity;
    private Context context;

    public AdapterAnuncio(List<Anuncio> anuncios, String nameActivity, Context context) {
        this.anuncios = anuncios;
        this.nameActivity = nameActivity;
        this.context = context;

    }

    @NonNull
    @Override
    public AdapterAnuncio.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.adapter_anuncios, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAnuncio.ViewHolder holder, int position) {
        final Anuncio anuncio = anuncios.get(position);

        holder.servico.setText(anuncio.getServico());
        holder.descricao.setText(anuncio.getDescricao());
        holder.telefone.setText(anuncio.getTelefone());
        holder.local.setText(anuncio.getLocal());
        holder.usuario.setText(anuncio.getUsuario());
        Picasso.with(this.context)
                .load(anuncio.getImage())
                .transform(new PicassoCircleTransformation())
                .into(holder.image);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (nameActivity.equals("HomeActivity")) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    callIntent.setData(Uri.parse("tel://" + anuncio.getTelefone()));
                    context.startActivity(callIntent);
                } else if (nameActivity.equals("MeusAnunciosActivity")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(anuncio.getUsuario());
                    builder.setMessage("Você deseja excluir esse anuncio?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("anuncios").child(anuncio.getuId());
                            dR.removeValue();
                            Toast.makeText(v.getContext(), "Anuncio deletado", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView servico;
        private TextView descricao;
        private TextView telefone;
        private TextView local;
        private TextView usuario;
        private RelativeLayout relativeLayout;
        private ImageView image;

        ViewHolder(View v) {
            super(v);
            usuario = v.findViewById(R.id.usuario);
            servico = v.findViewById(R.id.servico);
            descricao = v.findViewById(R.id.descricao);
            telefone = v.findViewById(R.id.telefone);
            local = v.findViewById(R.id.local);
            relativeLayout = v.findViewById(R.id.layout);
            image = v.findViewById(R.id.image);
        }

    }
}
