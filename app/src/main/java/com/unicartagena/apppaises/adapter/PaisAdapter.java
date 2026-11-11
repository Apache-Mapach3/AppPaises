package com.unicartagena.apppaises.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unicartagena.apppaises.R;
import com.unicartagena.apppaises.api.RetrofitClient;
import com.unicartagena.apppaises.model.Pais;
import com.unicartagena.apppaises.ui.PaisFormActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaisAdapter extends RecyclerView.Adapter<PaisAdapter.ViewHolder> {

    private List<Pais> paises;
    private SharedPreferences prefs;
    private Context context;

    public PaisAdapter(List<Pais> paises, SharedPreferences prefs, Context context) {
        this.paises = paises;
        this.prefs = prefs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pais, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pais pais = paises.get(position);
        holder.tvNombre.setText(pais.getNombre());
        holder.tvContinente.setText(pais.getContinente());

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaisFormActivity.class);
            intent.putExtra("pais", pais);
            context.startActivity(intent);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            String token = "Bearer " + prefs.getString("token", "");
            RetrofitClient.getApiService().eliminarPais(token, pais.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        paises.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "País eliminado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return paises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvContinente;
        Button btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePais);
            tvContinente = itemView.findViewById(R.id.tvContinente);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}