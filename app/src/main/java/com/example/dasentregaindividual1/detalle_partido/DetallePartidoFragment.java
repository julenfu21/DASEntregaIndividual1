package com.example.dasentregaindividual1.detalle_partido;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dasentregaindividual1.R;

public class DetallePartidoFragment extends Fragment {

    /* Atributos de la interfaz gráfica */
    private TextView puntosEquipoLocalTV;
    private ImageView escudoEquipoLocalIV;
    private TextView puntosEquipoVisitanteTV;
    private ImageView escudoEquipoVisitanteIV;
    private Button botonVerResumen;

    /* Otros atributos */
    private int puntosEquipoLocal, escudoIdEquipoLocal;
    private int puntosEquipoVisitante, escudoIdEquipoVisitante;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* RECUPERAR DATOS DEL PARTIDO SELECCIONADO */
        if (getArguments() != null) {
            puntosEquipoLocal = getArguments().getInt("idEquipoLocal");
            puntosEquipoVisitante = getArguments().getInt("idEquipoVisitante");
        }
    }

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
        // puntosEquipoLocalTV.setText(String.valueOf(puntosEquipoLocal));
        puntosEquipoLocalTV.setText(String.valueOf(puntosEquipoLocal));
        escudoEquipoLocalIV = view.findViewById(R.id.escudo_equipo_local);
        escudoEquipoLocalIV.setImageResource(escudoIdEquipoLocal);

        puntosEquipoVisitanteTV = view.findViewById(R.id.puntos_equipo_visitante);
        // puntosEquipoVisitanteTV.setText(String.valueOf(puntosEquipoVisitante));
        puntosEquipoVisitanteTV.setText(String.valueOf(puntosEquipoVisitante));
        escudoEquipoVisitanteIV = view.findViewById(R.id.escudo_equipo_visitante);
        escudoEquipoVisitanteIV.setImageResource(escudoIdEquipoVisitante);

        botonVerResumen = view.findViewById(R.id.boton_resumen);
        botonVerResumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // INTENT IMPLÍCITO PARA ABRIR YT
            }
        });
    }
}
