package com.example.petcare.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.R;
import com.example.petcare.dto.EventoDTO;

import java.util.ArrayList;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private ArrayList<EventoDTO> eventos;

    public EventoAdapter(ArrayList<EventoDTO> eventos) {

        this.eventos = eventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View elemento =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.celda_evento, parent, false);

        return new EventoViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {

        EventoDTO evento = eventos.get(position);

        holder.txtDiaEvento.setText(obtenerDia(evento.getFecha()));
        holder.txtMesEvento.setText(obtenerMes(evento.getFecha()));

        holder.txtTituloEvento.setText(evento.getTitulo());
        holder.txtDescripcionEvento.setText(evento.getDescripcion());
        holder.txtHoraEvento.setText(evento.getHora());
    }

    @Override
    public int getItemCount() {

        return eventos.size();
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {

        TextView txtDiaEvento;
        TextView txtMesEvento;
        TextView txtTituloEvento;
        TextView txtDescripcionEvento;
        TextView txtHoraEvento;
        TextView txtFlechaEvento;

        public EventoViewHolder(@NonNull View itemView) {

            super(itemView);

            txtDiaEvento = itemView.findViewById(R.id.txtDiaEvento);
            txtMesEvento = itemView.findViewById(R.id.txtMesEvento);
            txtTituloEvento = itemView.findViewById(R.id.txtTituloEvento);
            txtDescripcionEvento = itemView.findViewById(R.id.txtDescripcionEvento);
            txtHoraEvento = itemView.findViewById(R.id.txtHoraEvento);
            txtFlechaEvento = itemView.findViewById(R.id.txtFlechaEvento);

            itemView.setOnClickListener(view -> {

                int posicion = getBindingAdapterPosition();

                if(posicion != RecyclerView.NO_POSITION){

                    Toast.makeText(
                            view.getContext(),
                            eventos.get(posicion).getTitulo(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            itemView.setOnLongClickListener(view -> {

                int posicion = getBindingAdapterPosition();

                if(posicion != RecyclerView.NO_POSITION){

                    AlertDialog.Builder dialogo =
                            new AlertDialog.Builder(view.getContext());

                    dialogo.setTitle("Eliminar evento");
                    dialogo.setMessage("Desexas eliminar este evento?");

                    dialogo.setPositiveButton("Si", (dialogInterface, i) -> {

                        eventos.remove(posicion);

                        notifyItemRemoved(posicion);

                        Toast.makeText(
                                view.getContext(),
                                "Evento eliminado",
                                Toast.LENGTH_SHORT
                        ).show();
                    });

                    dialogo.setNegativeButton("Non", null);

                    dialogo.show();
                }

                return true;
            });
        }
    }

    private String obtenerDia(String fecha) {

        if(fecha != null && fecha.length() >= 2){

            return fecha.substring(0, 2);

        }

        return "--";
    }

    private String obtenerMes(String fecha) {

        if(fecha == null){

            return "---";
        }

        if(fecha.contains("/")){

            String[] partes = fecha.split("/");

            if(partes.length >= 2){

                return convertirMes(partes[1]);
            }
        }

        if(fecha.contains("-")){

            String[] partes = fecha.split("-");

            if(partes.length >= 2){

                return convertirMes(partes[1]);
            }
        }

        return "---";
    }

    private String convertirMes(String mes) {

        switch (mes) {

            case "01":
                return "XAN";

            case "02":
                return "FEB";

            case "03":
                return "MAR";

            case "04":
                return "ABR";

            case "05":
                return "MAI";

            case "06":
                return "XUÑ";

            case "07":
                return "XUL";

            case "08":
                return "AGO";

            case "09":
                return "SET";

            case "10":
                return "OUT";

            case "11":
                return "NOV";

            case "12":
                return "DEC";

            default:
                return "---";
        }
    }
}