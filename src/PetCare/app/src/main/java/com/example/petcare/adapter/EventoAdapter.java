package com.example.petcare.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.EventoCompletoActivity;
import com.example.petcare.R;
import com.example.petcare.dao.EventoDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.EventoDTO;

import java.util.ArrayList;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private ArrayList<EventoDTO> eventos;

    public EventoAdapter(ArrayList<EventoDTO> eventos) {

        this.eventos = eventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View elemento =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.celda_evento,
                                parent,
                                false
                        );

        return new EventoViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EventoViewHolder holder,
            int position
    ) {

        EventoDTO evento = eventos.get(position);

        // Datos da tarxeta
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

            // Referencias XML
            txtDiaEvento = itemView.findViewById(R.id.txtDiaEvento);
            txtMesEvento = itemView.findViewById(R.id.txtMesEvento);
            txtTituloEvento = itemView.findViewById(R.id.txtTituloEvento);
            txtDescripcionEvento = itemView.findViewById(R.id.txtDescripcionEvento);
            txtHoraEvento = itemView.findViewById(R.id.txtHoraEvento);
            txtFlechaEvento = itemView.findViewById(R.id.txtFlechaEvento);

            // Abrir evento completo
            itemView.setOnClickListener(view -> {

                int posicion = getBindingAdapterPosition();

                if (posicion != RecyclerView.NO_POSITION) {

                    EventoDTO evento = eventos.get(posicion);

                    Intent intent =
                            new Intent(
                                    view.getContext(),
                                    EventoCompletoActivity.class
                            );

                    // Enviamos o evento completo co seu id de Room
                    intent.putExtra("evento", evento);

                    view.getContext().startActivity(intent);
                }
            });

            // Borrar evento
            itemView.setOnLongClickListener(view -> {

                int posicion = getBindingAdapterPosition();

                if (posicion != RecyclerView.NO_POSITION) {

                    AlertDialog.Builder dialogo =
                            new AlertDialog.Builder(view.getContext());

                    dialogo.setTitle("Eliminar evento");
                    dialogo.setMessage("Desexas eliminar este evento?");

                    dialogo.setPositiveButton("Si", (dialogInterface, i) -> {

                        EventoDTO evento = eventos.get(posicion);

                        // Base de datos
                        EventoDAO dao =
                                PetCareDatabase
                                        .getInstance(view.getContext())
                                        .eventoDAO();

                        // Borrar en Room usando o id do evento
                        dao.borrar(evento);

                        // Borrar da lista visible
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

    // Obter día
    private String obtenerDia(String fecha) {

        if (fecha != null && fecha.length() >= 2) {

            return fecha.substring(0, 2);
        }

        return "--";
    }

    // Obter mes
    private String obtenerMes(String fecha) {

        if (fecha == null) {

            return "---";
        }

        if (fecha.contains("/")) {

            String[] datosFecha = fecha.split("/");

            if (datosFecha.length >= 2) {

                return convertirMes(datosFecha[1]);
            }
        }

        if (fecha.contains("-")) {

            String[] datosFecha = fecha.split("-");

            if (datosFecha.length >= 2) {

                return convertirMes(datosFecha[1]);
            }
        }

        return "---";
    }

    // Converter número de mes
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