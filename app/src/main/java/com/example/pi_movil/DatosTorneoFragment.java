package com.example.pi_movil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.example.pi_movil.datos.Torneo;
import com.example.pi_movil.dialogos.InsertarEquipoTorneoDialogo;
import com.example.pi_movil.dialogos.InvitarUsuarioDialogo;
import com.example.pi_movil.geocode.Direccion;
import com.example.pi_movil.tarjetas.TorneoAdapter;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ventura.bracketslib.model.ColomnData;
import com.ventura.bracketslib.model.CompetitorData;
import com.ventura.bracketslib.model.MatchData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;

public class DatosTorneoFragment extends Fragment implements InsertarEquipoTorneoDialogo.InsertarEquipoTorneoDialogoListener {

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

    Button btnInscribirse;
    Button btnRondas;

    Torneo t;

    String equipoEscogido;

    private String clientId = "AbnHRgc9bl4x9MgoqnGgyN7cb9Ubtd49SwLrsdJk3LxJeH2WEAoTLORCra9Qtz9sK9ZKTEV6mAFWbfu4";
    int PAYPAL_REQUEST_CODE= 123;
    public static PayPalConfiguration configuration;

    public DatosTorneoFragment(Torneo t) {
        this.t = t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_datos_torneo, container, false);

        nombre = view.findViewById(R.id.datoTorneoNombre);
        deporte = view.findViewById(R.id.datoTorneoDeporte);
        coste = view.findViewById(R.id.datoTorneoCoste);
        inscritos = view.findViewById(R.id.datoTorneoInscritos);
        ubicacion = view.findViewById(R.id.datoTorneoUbicacion);
        minEquipos = view.findViewById(R.id.datoTorneoMinEquipos);
        hInicio = view.findViewById(R.id.datoTorneoHoraInicio);
        fInicio = view.findViewById(R.id.datoTorneoFechaInicio);
        hLimite = view.findViewById(R.id.datoTorneoHoraLimite);
        fLimite = view.findViewById(R.id.datoTorneoFechaLimite);
        btnInscribirse = view.findViewById(R.id.datoTorneoInscribirse);
        btnRondas = view.findViewById(R.id.datoTorneoRondas);

        cargarTorneo();

        gestionarInscripccion();

        btnRondas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarRondas();
            }
        });

        return view;
    }

    private void cargarTorneo() {

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


    }

    private void cargarRondas() {

        DatosTorneoFragment.Tasks nTasks = new DatosTorneoFragment.Tasks();
        nTasks.execute(Mensajes.TOURNAMENTS_ROUNDS + ";" + t.getId());
    }


    private void gestionarInscripccion() {

        DatosTorneoFragment.Tasks nTasks = new DatosTorneoFragment.Tasks();
        nTasks.execute(Mensajes.TOURNAMENTS_STATE_USER + ";" + t.getId());

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
            if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_STATE_USER_OK)) {

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
                ((HomeActivity)getActivity()).completado();
            } else if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_STATE_USER_ERROR)) {
                Toast.makeText(getActivity(), "No se puede obtener datos sobre inscripcciones", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            } else if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_INSCRIBE_TEAM_USER_OK)) {
                Toast.makeText(getActivity(), "Inscripccion realizada", Toast.LENGTH_SHORT).show();
                btnInscribirse.setText("INSCRITO");
                btnInscribirse.setEnabled(false);
                t.nuevaInscripccion();
                inscritos.setText(t.inscritosMaxInscritos());
                ((HomeActivity)getActivity()).completado();
            } else if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_INSCRIBE_TEAM_USER_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo realizar la inscripccion", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();

            } else if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_ROUNDS_OK)) {
                //COMPROBAR SI TIENE RONDAS
                if (mensajeRespuesta.length==1){
                    ((HomeActivity)getActivity()).completado();
                    Toast.makeText(getActivity(), "Todavía no se han generado las rondas", Toast.LENGTH_SHORT).show();
                }else {
                    ArrayList<ColomnData> colomnData = new ArrayList<>();
                    String[] rondas = mensajeRespuesta[1].split(":");
                    for (int i = 0; i < rondas.length; i++) {
                        String[] equiposRonda = rondas[i].split("!");
                        ArrayList<MatchData> enfrentamientos = new ArrayList<>();
                        for (int j = 0; j < equiposRonda.length; j++) {
                            String[] partes = equiposRonda[j].split("/");
                            String id = partes[0];
                            String rondaEnfrentamiento = partes[1];
                            String eLocal = partes[2];
                            String ptosLocal = partes[3];
                            String eVisitante = partes[4];
                            String ptosVisitante = partes[5];

                            CompetitorData local = new CompetitorData(eLocal, ptosLocal);
                            CompetitorData visitante = new CompetitorData(eVisitante, ptosVisitante);
                            MatchData enfrentamiento = new MatchData(local, visitante);

                            enfrentamientos.add(enfrentamiento);

                            System.out.println(enfrentamientos);

                        }

                        colomnData.add(new ColomnData(enfrentamientos));
                        System.out.println(colomnData.size());
                    }

                    Intent intent = new Intent(getActivity(), DatosTorneoRondasActivity.class);

                    Bundle datosEnviar = new Bundle();
                    datosEnviar.putInt("id", t.getId());
                    datosEnviar.putSerializable("columnas", colomnData);
                    intent.putExtras(datosEnviar);

                    ((HomeActivity)getActivity()).completado();
                    startActivityForResult(intent, 0);
                }

            } else if(mensajeRespuesta[0].equals(Mensajes.INSERT_PAY_OK)) {
                int idPago = Integer.parseInt(mensajeRespuesta[1]);
                insertarLigaPago(idPago);
                ((HomeActivity)getActivity()).completado();
            }else if(mensajeRespuesta[0].equals(Mensajes.INSERT_PAY_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo almacenar el pago", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else if(mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_INSCRIBE_USER_PAY_OK)) {
                Toast.makeText(getActivity(), "Inscripccion realizada", Toast.LENGTH_SHORT).show();
                btnInscribirse.setText("INSCRITO");
                btnInscribirse.setEnabled(false);
                t.nuevaInscripccion();
                inscritos.setText(t.inscritosMaxInscritos());
                ((HomeActivity)getActivity()).completado();
            }
        }
    }

    public void inscribirEquipo() {
        InsertarEquipoTorneoDialogo dialogo = new InsertarEquipoTorneoDialogo(this, t.getId());
        dialogo.show(getActivity().getSupportFragmentManager(), "Insertar equipo");
    }

    @Override
    public void applyTexts(String nombreEquipo) {
        String equipo = nombreEquipo;
        equipoEscogido = nombreEquipo;
        Log.i("EQUIPO DEVUELTO ", nombreEquipo);

        if(t.getCoste()>0){
            configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(clientId);
            PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(t.getCoste())),"EUR","Inscripccion a torneo:",PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);

            startActivityForResult(intent,PAYPAL_REQUEST_CODE);

        }else {
            DatosTorneoFragment.Tasks nTasks = new DatosTorneoFragment.Tasks();
            nTasks.execute(Mensajes.TOURNAMENTS_INSCRIBE_TEAM_USER + ";" + t.getId() + ";" + equipo);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQUEST_CODE){
            try {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {

                    DatosTorneoFragment.Tasks nTasks = new DatosTorneoFragment.Tasks();
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

        DatosTorneoFragment.Tasks nTasks = new DatosTorneoFragment.Tasks();
        nTasks.execute(Mensajes.TOURNAMENTS_INSCRIBE_USER_PAY + ";" + t.getId() + ";" + equipoEscogido +";"+idPago);
    }

}