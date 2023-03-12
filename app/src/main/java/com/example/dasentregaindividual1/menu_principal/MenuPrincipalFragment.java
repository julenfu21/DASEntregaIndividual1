package com.example.dasentregaindividual1.menu_principal;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dasentregaindividual1.R;
import com.example.dasentregaindividual1.data.base_de_datos.BaseDeDatos;

public class MenuPrincipalFragment extends Fragment {

    /* Atributos de la interfaz gráfica */
    private Button botonJornadas;
    private Button botonClasificacion;
    private Button botonAjustes;
    private Button botonSalir;

    /* Otros atributos */
    private SQLiteDatabase baseDeDatos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MenuPrincipalFragment", "onCreate");

        // Recuperar instancia de la base de datos
        BaseDeDatos gestorBD = new BaseDeDatos (requireContext(), "Euroliga",
                null, 1);
        baseDeDatos = gestorBD.getWritableDatabase();

        // Listener para actuar en función de la respuesta en el diálogo
        getParentFragmentManager().setFragmentResultListener(
            "opcionSalir", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                int opcionSalirId = bundle.getInt("opcionSalirId");
                switch (opcionSalirId) {
                    case 1:
                        cerrarSesion();
                        requireActivity().finish();
                        break;
                    case 2:
                        // Nothing TO-DO
                        break;
                    case 3:
                        cerrarSesion();
                        navegarHaciaLogin();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        Log.d("MenuPrincipalFragment", "onCreateView");
        return inflater.inflate(R.layout.fragment_menu_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("MenuPrincipalFragment", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        botonJornadas = view.findViewById(R.id.boton_jornadas);
        botonJornadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarHaciaJornadas(view);
            }
        });

        botonClasificacion = view.findViewById(R.id.boton_clasificacion);
        botonClasificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarHaciaClasificacion(view);
            }
        });

        botonAjustes = view.findViewById(R.id.boton_ajustes);
        botonAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarHaciaAjustes(view);
            }
        });

        botonSalir = view.findViewById(R.id.salir);
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DIALOGO CERRAR SESIÓN
                DialogFragment dialogoSalir = new SalirDialogFragment();
                dialogoSalir.show(getParentFragmentManager(), "dialogo_salir");
            }
        });
    }

    private void cerrarSesion() {
        /*
        UPDATE Usuario SET sesion_iniciada = 0
        WHERE nombre_usuario = ?
        */
        SharedPreferences preferencias = PreferenceManager
            .getDefaultSharedPreferences(requireContext());
        String usuario = preferencias.getString("Usuario", null);
        ContentValues iniciarSesion = new ContentValues();
        iniciarSesion.put("sesion_iniciada", 0);
        String[] argumentos = new String[] {usuario};
        baseDeDatos.update("Usuario", iniciarSesion,
                "nombre_usuario = ?", argumentos);
    }

    private void navegarHaciaLogin() {
        NavDirections accion = MenuPrincipalFragmentDirections
            .actionMenuPrincipalFragmentToLoginFragment();
        NavHostFragment.findNavController(this).navigate(accion);
    }

    private void navegarHaciaJornadas(View view) {
        NavDirections accion = MenuPrincipalFragmentDirections
            .actionMenuPrincipalFragmentToListaPartidosFragment();
        Navigation.findNavController(view).navigate(accion);
    }

    private void navegarHaciaClasificacion(View view) {
        NavDirections accion = MenuPrincipalFragmentDirections
            .actionMenuPrincipalFragmentToClasificacionFragment();
        Navigation.findNavController(view).navigate(accion);
    }

    private void navegarHaciaAjustes(View view) {
        NavDirections accion = MenuPrincipalFragmentDirections
            .actionMenuPrincipalFragmentToAjustesFragment();
        Navigation.findNavController(view).navigate(accion);
    }
}