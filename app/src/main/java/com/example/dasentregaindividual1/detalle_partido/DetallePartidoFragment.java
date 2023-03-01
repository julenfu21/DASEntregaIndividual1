package com.example.dasentregaindividual1.detalle_partido;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dasentregaindividual1.R;

public class DetallePartidoFragment extends Fragment {

    private TextView puntosEquipoLocalTV;
    private ImageView escudoEquipoLocalIV;

    private TextView puntosEquipoVisitanteTV;
    private ImageView escudoEquipoVisitanteIV;


    private int puntosEquipoLocal, escudoIdEquipoLocal;
    private int puntosEquipoVisitante, escudoIdEquipoVisitante;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        Log.d("DetallePartidoFragment", "onCreateView");
        return inflater.inflate(R.layout.fragment_detalle_partido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("DetallePartidoFragment", "onViewCreated");

        puntosEquipoLocalTV = view.findViewById(R.id.puntos_equipo_local);
        puntosEquipoLocalTV.setText(String.valueOf(puntosEquipoLocal));
        escudoEquipoLocalIV = view.findViewById(R.id.escudo_equipo_local);
        escudoEquipoLocalIV.setImageResource(escudoIdEquipoLocal);

        puntosEquipoVisitanteTV = view.findViewById(R.id.puntos_equipo_visitante);
        puntosEquipoVisitanteTV.setText(String.valueOf(puntosEquipoVisitante));
        escudoEquipoVisitanteIV = view.findViewById(R.id.escudo_equipo_visitante);
        escudoEquipoVisitanteIV.setImageResource(escudoIdEquipoVisitante);
    }

    public void actualizarDatos(
        int puntosEquipoLocal,
        int escudoIdEquipoLocal,
        int puntosEquipoVisitante,
        int escudoIdEquipoVisitante
    ) {
        if (puntosEquipoLocalTV != null) {
            puntosEquipoLocalTV.setText(puntosEquipoLocal);
            escudoEquipoLocalIV.setImageResource(escudoIdEquipoLocal);
            puntosEquipoVisitanteTV.setText(puntosEquipoVisitante);
            escudoEquipoVisitanteIV.setImageResource(escudoIdEquipoVisitante);
        } else {
            this.puntosEquipoLocal = puntosEquipoLocal;
            this.escudoIdEquipoLocal = escudoIdEquipoLocal;
            this.puntosEquipoVisitante = puntosEquipoVisitante;
            this.escudoIdEquipoVisitante = escudoIdEquipoVisitante;
        }
    }
}
