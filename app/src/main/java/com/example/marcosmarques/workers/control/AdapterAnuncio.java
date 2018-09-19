package com.example.marcosmarques.workers.control;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.marcosmarques.workers.R;
import com.example.marcosmarques.workers.model.Anuncio;

import java.util.List;

public class AdapterAnuncio extends RecyclerView.Adapter<AdapterAnuncio.ViewHolder> {

    private List<Anuncio> anuncios;

    public AdapterAnuncio(List<Anuncio> anuncios) {
        this.anuncios = anuncios;

    }

    @NonNull
    @Override
    public AdapterAnuncio.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.adapter_anuncios, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAnuncio.ViewHolder holder, int position) {
        final Anuncio anuncio = anuncios.get(position);

        holder.servico.setText(anuncio.getServico());
        holder.descricao.setText(anuncio.getDescricao());
        holder.telefone.setText(anuncio.getTelefone());
        holder.local.setText(anuncio.getLocal());
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView servico;
        private TextView descricao;
        private TextView telefone;
        private TextView local;

        public ViewHolder(View v) {
            super(v);
            servico = (TextView) v.findViewById(R.id.servico);
            descricao = (TextView) v.findViewById(R.id.descricao);
            telefone = (TextView) v.findViewById(R.id.telefone);
            local = (TextView) v.findViewById(R.id.local);
        }

    }
}
