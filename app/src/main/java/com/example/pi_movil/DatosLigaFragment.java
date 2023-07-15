package com.example.pi_movil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Liga;
import com.example.pi_movil.dialogos.InsertarEquipoLigaDialogo;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;

public class DatosLigaFragment extends Fragment implements InsertarEquipoLigaDialogo.InsertarEquipoLigaDialogoListener{

    TextView nombre;
    TextView deporte;
    TextView coste;
    TextView inscritos;
    TextView ubicacion;
    TextView minEquipos;
    TextView hInicio;
    TextView fInicio;
    TextView hLimite;
    TextView fLimite;
    TextView frecuanciaJornada;
    TextView duracionPartidos;
    TextView hInicioPartidos;
    TextView fFinPartidos;

    Button btnInscribirse;

    Liga t;

    String equipoEscogido;

    private String clientId = "AbnHRgc9bl4x9MgoqnGgyN7cb9Ubtd49SwLrsdJk3LxJeH2WEAoTLORCra9Qtz9sK9ZKTEV6mAFWbfu4";
    int PAYPAL_REQUEST_CODE= 123;
    public static PayPalConfiguration configuration;

    public DatosLigaFragment(Liga t) {
        this.t = t;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_liga, container, false);

        nombre = view.findViewById(R.id.datoLigaNombre);
        deporte = view.findViewById(R.id.datoLigaDeporte);
        coste = view.findViewById(R.id.datoLigaCoste);
        inscritos = view.findViewById(R.id.datoLigaInscritos);
        ubicacion = view.findViewById(R.id.datoLigaUbicacion);
        minEquipos = view.findViewById(R.id.datoLigaMinEquipos);
        hInicio = view.findViewById(R.id.datoLigaHoraInicio);
        fInicio = view.findViewById(R.id.datoLigaFechaInicio);
        hLimite = view.findViewById(R.id.datoLigaHoraLimite);
        fLimite = view.findViewById(R.id.datoLigaFechaLimite);
        frecuanciaJornada = view.findViewById(R.id.datoLigaFrecuenciaJornada);
        duracionPartidos = view.findViewById(R.id.datoLigaDuracionPartidos);
        hInicioPartidos = view.findViewById(R.id.datoLigaHoraInicioPartidos);
        fFinPartidos = view.findViewById(R.id.datoLigaHoraFinPartidos);

        btnInscribirse = view.findViewById(R.id.datoLigaInscribirse);

        cargarLiga();

        gestionarInscripccion();


        return view;
    }

    private void cargarLiga() {

        nombre.setText(t.getNombre());
        deporte.setText(t.getDeporte());
        coste.setText(t.getCoste() + "€");
        inscritos.setText(t.inscritosMaxInscritos());
        ubicacion.setText(t.getUbicacion().toString());
        minEquipos.setText("" + t.getMinEquipos());
        hInicio.setText(t.getHoraInicio());
        fInicio.setText(t.getFechaInicio());
        hLimite.setText(t.getHoraLimite());
        fLimite.setText(t.getFechaLimite());
        frecuanciaJornada.setText(t.getFrecuenciaJornada()+"");
        duracionPartidos.setText(t.getDuracionPartidos()+"");
        hInicioPartidos.setText(t.gethInicioPartidos()+"");
        fFinPartidos.setText(t.gethFinPartidos()+"");


    }

    private void gestionarInscripccion() {

        DatosLigaFragment.Tasks nTasks = new DatosLigaFragment.Tasks();
        nTasks.execute(Mensajes.LIGUES_STATE_USER + ";" + t.getId());

    }

    class Tasks extends AsyncTasks<String, Float, String> {
        Socket s;
        PrintWriter out = null;
        BufferedReader in = null;

        @Override
        public void onPreExecute() {
            s = Session.getSocket();
            ((HomeActivity)getActivity()).cargando();
        }

        @Override
        public String doInBackground(String... strings) {
            Log.v("Enviar: ", strings[0]);

            String recibido = "";
            try {
                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                out.println(strings[0]);
                try {
                    recibido = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (recibido != null)
                    Log.v("Recibido del servidor: ", recibido);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "El servidor esta offline", Toast.LENGTH_SHORT).show();
            }

            return recibido;
        }

        @Override
        public void onPostExecute(String respuesta) {

            Log.v("RESPUESTA DaToFrag", respuesta);
            String[] mensajeRespuesta = respuesta.split(";");
            if (mensajeRespuesta[0].equals(Mensajes.LIGUES_STATE_USER_OK)) {

                String valorBoton = mensajeRespuesta[1];
                btnInscribirse.setText(valorBoton);

                switch (valorBoton) {

                    case "INSCRITO":
                        btnInscribirse.setEnabled(false);
                        break;
                    case "INSCRIBIRSE":
                        btnInscribirse.setEnabled(true);
                        btnInscribirse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                inscribirEquipo();
                            }
                        });

                        break;
                    case "CERRADO":
                        btnInscribirse.setEnabled(false);
                        break;
                }
            } else if (mensajeRespuesta[0].equals(Mensajes.LIGUES_STATE_USER_ERROR)) {
                Toast.makeText(getActivity(), "No se puede obtener datos sobre inscripcciones", Toast.LENGTH_SHORT).show();
            } else if (mensajeRespuesta[0].equals(Mensajes.LIGUES_INSCRIBE_TEAM_USER_OK)) {
                Toast.makeText(getActivity(), "Inscripccion realizada", Toast.LENGTH_SHORT).show();
                btnInscribirse.setText("INSCRITO");
                btnInscribirse.setEnabled(false);
                t.nuevaInscripccion();
                inscritos.setText(t.inscritosMaxInscritos());
            } else if (mensajeRespuesta[0].equals(Mensajes.LIGUES_INSCRIBE_TEAM_USER_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo realizar la inscripccion", Toast.LENGTH_SHORT).show();
            } else if(mensajeRespuesta[0].equals(Mensajes.INSERT_PAY_OK)) {
                int idPago = Integer.parseInt(mensajeRespuesta[1]);
                insertarLigaPago(idPago);
            }else if(mensajeRespuesta[0].equals(Mensajes.INSERT_PAY_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo almacenar el pago", Toast.LENGTH_SHORT).show();
            }else if(mensajeRespuesta[0].equals(Mensajes.LIGUES_INSCRIBE_USER_PAY_OK)) {
                Toast.makeText(getActivity(), "Inscripccion realizada", Toast.LENGTH_SHORT).show();
                btnInscribirse.setText("INSCRITO");
                btnInscribirse.setEnabled(false);
                t.nuevaInscripccion();
                inscritos.setText(t.inscritosMaxInscritos());
            }
            ((HomeActivity)getActivity()).completado();
        }
    }

    public void inscribirEquipo() {
        InsertarEquipoLigaDialogo dialogo = new InsertarEquipoLigaDialogo(this, t.getId());
        dialogo.show(getActivity().getSupportFragmentManager(), "Insertar equipo");
    }

    @Override
    public void applyTexts(String nombreEquipo) {
        String equipo = nombreEquipo;
        equipoEscogido = nombreEquipo;
        Log.i("EQUIPO DEVUELTO ", nombreEquipo);

        if(t.getCoste()>0){
            configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(clientId);
            PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(t.getCoste())),"EUR","Inscripccion a liga:",PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);

            startActivityForResult(intent,PAYPAL_REQUEST_CODE);

        }else{

            DatosLigaFragment.Tasks nTasks = new DatosLigaFragment.Tasks();
            nTasks.execute(Mensajes.LIGUES_INSCRIBE_TEAM_USER + ";" + t.getId() + ";" + equipo);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQUEST_CODE){
            try {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {

                    DatosLigaFragment.Tasks nTasks = new DatosLigaFragment.Tasks();
                    nTasks.execute(Mensajes.INSERT_PAY + ";" + t.getCoste());

                } else if (requestCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Pago Cancelado", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){

            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(getContext(),"Pago Invalido",Toast.LENGTH_SHORT).show();

        }
    }

    private void insertarLigaPago(int idPago){

        DatosLigaFragment.Tasks nTasks = new DatosLigaFragment.Tasks();
        nTasks.execute(Mensajes.LIGUES_INSCRIBE_USER_PAY + ";" + t.getId() + ";" + equipoEscogido +";"+idPago);
    }
}