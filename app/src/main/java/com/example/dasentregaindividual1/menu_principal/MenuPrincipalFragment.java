package com.example.dasentregaindividual1.menu_principal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import com.example.dasentregaindividual1.R;
import com.example.dasentregaindividual1.data.base_de_datos.BaseDeDatos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MenuPrincipalFragment extends Fragment {

    /* Atributos de la interfaz gráfica */
    private Button botonJornadas;
    private Button botonClasificacion;
    private Button botonAjustes;
    private Button botonSalir;
    private Button botonRecomendarApk;

    /* Otros atributos */
    private SQLiteDatabase baseDeDatos;
    private ListenerMenuPrincipalFragment listenerMenuPrincipalFragment;


    /*
     * Interfaz para que 'MainActivity' implemente la notificación del partido y que aparezca al
     * acceder a este fragmento.
     */
    public interface ListenerMenuPrincipalFragment {
        void crearNotificacionesPartido(ArrayList<String> listaEquiposFavoritos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Recuperar instancia de la base de datos */
        BaseDeDatos gestorBD = new BaseDeDatos (requireContext(), "Euroliga",
            null, 1);
        baseDeDatos = gestorBD.getWritableDatabase();

        /*
        * Listener para actuar de una forma determinada en función del número enviado por el
        * diálogo 'SalirDialogFragment'.
        */
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

        crearNotificacionPartido();
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        /* Para que la flecha del 'app bar' desaparezca (produce un efecto no deseado) */
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
           actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return inflater.inflate(R.layout.fragment_menu_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                mostrarDialogoSalir();
            }
        });

        botonRecomendarApk = view.findViewById(R.id.boton_recomendar);
        botonRecomendarApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recomendarAplicacion();
            }
        });

        agregarFuncionABotonAtras();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listenerMenuPrincipalFragment = (ListenerMenuPrincipalFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("La clase " + context
                + "debe implementar ListenerMenuPrincipalFragment");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseDeDatos.close();
    }

    private void cerrarSesion() {
        SharedPreferences preferencias = PreferenceManager
            .getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("usuario", null);
        editor.apply();
    }

    private void crearNotificacionPartido() {
        /*
        SELECT nombre_equipo FROM Favorito
        WHERE nombre_usuario = ?
        */
        SharedPreferences preferencias = PreferenceManager
            .getDefaultSharedPreferences(requireContext());
        String usuario = preferencias.getString("usuario", null);
        String[] campos = new String[] {"nombre_equipo"};
        String[] argumentos = new String[] {usuario};
        Cursor cEquipoFavorito = baseDeDatos.query("Favorito", campos,
            "nombre_usuario = ?", argumentos, null, null, null);

        ArrayList<String> listaEquiposFavoritos = new ArrayList<>();
        while (cEquipoFavorito.moveToNext()) {
            String equipo = cEquipoFavorito.getString(0);
            listaEquiposFavoritos.add(equipo);
        }
        cEquipoFavorito.close();
        listenerMenuPrincipalFragment.crearNotificacionesPartido(listaEquiposFavoritos);
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

    private void mostrarDialogoSalir() {
        DialogFragment dialogoSalir = new SalirDialogFragment();
        dialogoSalir.show(getParentFragmentManager(), "dialogo_salir");
    }

    private void recomendarAplicacion() {
        String textoRecomendacion = extraerTextoRecomendacionDeFichero();
        crearIntentEmail(textoRecomendacion);
    }

    private String extraerTextoRecomendacionDeFichero() {
        String plantillaMensaje = leerPlantillaDelMensaje();
        return personalizarMensaje(plantillaMensaje);
    }

    /*
    * Mediante esta función se lee el mensaje de recomendación de la aplicación desde un fichero
    * interno. Es necesario tener en cuenta que se escoge la plantilla en función del idioma
    * seleccionado en las preferencias de la aplicación
    */
    private String leerPlantillaDelMensaje() {
        SharedPreferences preferencias = PreferenceManager
            .getDefaultSharedPreferences(requireContext());
        String idioma = preferencias.getString("idioma", null);
        InputStream fich = null;
        switch (idioma) {
            case "Euskara":
                fich = getResources().openRawResource(R.raw.recomendar_apk_eu);
                break;
            case "Castellano":
                fich = getResources().openRawResource(R.raw.recomendar_apk_es);
                break;
            case "English":
                fich = getResources().openRawResource(R.raw.recomendar_apk_en);
                break;
        }
        BufferedReader buff = new BufferedReader(new InputStreamReader(fich));
        StringBuilder plantillaMensaje = new StringBuilder();
        try {
            String linea = buff.readLine();
            while (linea != null) {
                if (linea.equals(""))
                    plantillaMensaje.append("\n\n");
                else
                    plantillaMensaje.append(linea);
                linea = buff.readLine();
            }
            fich.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plantillaMensaje.toString();
    }

    /*
     * Mediante esta función, en primera instancia se accede al nombre de usuario que ha iniciado
     * sesión mediante las prefererencias y a continuación se introduce dentro del mensaje
     * sustituyendo '%s' con este.
     */
    private String personalizarMensaje(String plantillaMensaje) {
        SharedPreferences preferencias = PreferenceManager
            .getDefaultSharedPreferences(requireContext());
        String usuario = preferencias.getString("usuario", null);
        return String.format(plantillaMensaje, usuario);
    }

    private void crearIntentEmail(String textoMensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.recomendacion_apk_asunto));
        emailIntent.putExtra(Intent.EXTRA_TEXT, textoMensaje);
        startActivity(Intent.createChooser(emailIntent, getString(R.string.recomendar_apk_via)));
    }

    private void agregarFuncionABotonAtras() {
        requireActivity().getOnBackPressedDispatcher()
            .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    mostrarDialogoSalir();
                }
            });
    }
}