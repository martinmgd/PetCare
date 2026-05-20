package com.example.petcare.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.MascotaCompletaActivity;
import com.example.petcare.R;
import com.example.petcare.dto.MascotaDTO;

import java.util.ArrayList;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    // Lista de mascotas
    private ArrayList<MascotaDTO> mascotas;

    public MascotaAdapter(ArrayList<MascotaDTO> mascotas) {
        this.mascotas = mascotas;
    }

    // Crea cada celda
    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(R.layout.celda_mascota, parent, false);
        return new MascotaViewHolder(elemento);
    }

    // Asigna datos a la celda
    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        MascotaDTO mascota = mascotas.get(position);

        holder.ivMascota.setImageResource(R.drawable.ic_launcher_foreground);
        holder.txtNombreMascota.setText(mascota.getNombre());
        holder.txtDatosMascota.setText(mascota.getEspecie() + " - " + mascota.getRaza() + " - " + mascota.getEdad() + " años");
    }

    // Número de elementos
    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public class MascotaViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMascota;
        TextView txtNombreMascota;
        TextView txtDatosMascota;

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMascota = itemView.findViewById(R.id.ivMascota);
            txtNombreMascota = itemView.findViewById(R.id.txtNombreMascota);
            txtDatosMascota = itemView.findViewById(R.id.txtDatosMascota);

            // Abrir ficha completa
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int posicion = getBindingAdapterPosition();

                    if(posicion != RecyclerView.NO_POSITION){

                        Intent intent = new Intent(view.getContext(), MascotaCompletaActivity.class);
                        intent.putExtra("mascota", mascotas.get(posicion));
                        intent.putExtra("posicion", posicion);

                        view.getContext().startActivity(intent);
                    }
                }
            });

            // Borrar mascota
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int posicion = getBindingAdapterPosition();

                    if(posicion != RecyclerView.NO_POSITION){

                        AlertDialog.Builder dialogo = new AlertDialog.Builder(view.getContext());

                        dialogo.setTitle("Eliminar mascota");
                        dialogo.setMessage("¿Deseas eliminar esta mascota?");

                        dialogo.setPositiveButton("Sí", (dialogInterface, i) -> {

                            mascotas.remove(posicion);
                            notifyItemRemoved(posicion);

                            Toast.makeText(view.getContext(), "Mascota eliminada", Toast.LENGTH_SHORT).show();
                        });
                        dialogo.setNegativeButton("No", null);
                        dialogo.show();
                    }
                    return true;
                }
            });
        }
    }
}