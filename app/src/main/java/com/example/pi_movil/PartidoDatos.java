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
import com.example.pi_movil.datos.Partido;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ventura.bracketslib.model.ColomnData;
import com.ventura.bracketslib.model.CompetitorData;
import com.ventura.bracketslib.model.MatchData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;

public class PartidoDatos extends Fragment {

    TextView deporte, ubicacion, fInicio, hInicio, fLimite, hLimite, inscritos, coste;
    Button inscribirse;
    Partido actual;

    private String clientId = "AbnHRgc9bl4x9MgoqnGgyN7cb9Ubtd49SwLrsdJk3LxJeH2WEAoTLORCra9Qtz9sK9ZKTEV6mAFWbfu4";
    int PAYPAL_REQUEST_CODE= 123;
    public static PayPalConfiguration configuration;

    public PartidoDatos(Partido actual) {
        this.actual = actual;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_partido_datos, container, false);

        deporte = view.findViewById(R.id.infoPartidoDeportes);
        ubicacion = view.findViewById(R.id.infoPartidoUbicacion);
        fInicio = view.findViewById(R.id.infoPartidofInicio);
        hInicio = view.findViewById(R.id.infoPartidohInicio);
        fLimite = view.findViewById(R.id.infoPartidofLimite);
        hLimite = view.findViewById(R.id.infoPartidohLimite);
        inscritos = view.findViewById(R.id.infoPartidoInscritos);
        coste = view.findViewById(R.id.infoPartidoCoste);

        cargarPartido();

        inscribirse = view.findViewById(R.id.infoPartidoInscribirse);
        cargarInscripccion();

        return view;
    }

    private void cargarPartido() {
        deporte.setText(actual.getDeporte());
        ubicacion.setText(actual.getUbicacion().toString());
        fInicio.setText(actual.getFechaInicio());
        hInicio.setText(actual.getHoraInicio());
        fLimite.setText(actual.getFechaLimite());
        hLimite.setText(actual.getHoraLimite());
        inscritos.setText(actual.getInscritos()+"/"+ actual.getMaxInscritos());
        coste.setText(actual.getCoste()+" €");
    }

    private void cargarInscripccion(){
        PartidoDatos.Tasks nTasks = new PartidoDatos.Tasks();
        nTasks.execute(Mensajes.GAMES_STATE_USER + ";" + actual.getId());
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
            if (mensajeRespuesta[0].equals(Mensajes.GAMES_STATE_USER_OK)) {

                String valorBoton = mensajeRespuesta[1];
                inscribirse.setText(valorBoton);

                switch (valorBoton) {

                    case "INSCRITO":
                        inscribirse.setEnabled(false);
                        break;
                    case "INSCRIBIRSE":
                        inscribirse.setEnabled(true);
                        inscribirse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                inscribirUsuario();
                            }
                        });

                        break;
                    case "CERRADO":
                        inscribirse.setEnabled(false);
                        break;
                }
                ((HomeActivity)getActivity()).completado();

            } else if (mensajeRespuesta[0].equals(Mensajes.GAMES_STATE_USER_ERROR)) {
                Toast.makeText(getActivity(), "No se puede obtener datos sobre inscripcciones", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();

            } else if (mensajeRespuesta[0].equals(Mensajes.GAMES_INSCRIBE_USER_OK)) {
                Toast.makeText(getActivity(), "Inscripccion realizada", Toast.LENGTH_SHORT).show();
                inscribirse.setText("INSCRITO");
                inscribirse.setEnabled(false);
                inscritos.setText((actual.getInscritos()+1)+"/"+ actual.getMaxInscritos());
                ((HomeActivity)getActivity()).completado();

            } else if (mensajeRespuesta[0].equals(Mensajes.GAMES_INSCRIBE_USER_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo realizar la inscripccion", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();

            }else if(mensajeRespuesta[0].equals(Mensajes.INSERT_PAY_OK)) {
                int idPago = Integer.parseInt(mensajeRespuesta[1]);
                insertarPartidoPago(idPago);
                ((HomeActivity)getActivity()).completado();

            }else if(mensajeRespuesta[0].equals(Mensajes.INSERT_PAY_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo almacenar el pago", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();

            }else if(mensajeRespuesta[0].equals(Mensajes.GAMES_INSCRIBE_USER_PAY_OK)) {
                Toast.makeText(getActivity(), "Inscripccion realizada", Toast.LENGTH_SHORT).show();
                inscribirse.setText("INSCRITO");
                inscribirse.setEnabled(false);
                inscritos.setText((actual.getInscritos()+1)+"/"+ actual.getMaxInscritos());
                ((HomeActivity)getActivity()).completado();

            }else if(mensajeRespuesta[0].equals(Mensajes.GAMES_INSCRIBE_USER_PAY_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo inscribir", Toast.LENGTH_SHORT).show();
                ((HomeActivity) getActivity()).completado();

            }
        }
    }

    private void insertarPartidoPago(int idPago){

        PartidoDatos.Tasks nTasks = new PartidoDatos.Tasks();
        nTasks.execute(Mensajes.GAMES_INSCRIBE_USER_PAY + ";" + actual.getId()+";"+idPago);
    }

    private void inscribirUsuario(){
        if(actual.getCoste()>0){
            configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(clientId);
            PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(actual.getCoste())),"EUR","Inscripccion a partido:",PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);

            startActivityForResult(intent,PAYPAL_REQUEST_CODE);

        }else{
            PartidoDatos.Tasks nTasks = new PartidoDatos.Tasks();
            nTasks.execute(Mensajes.GAMES_INSCRIBE_USER + ";" + actual.getId());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQUEST_CODE){
            try {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {

                    PartidoDatos.Tasks nTasks = new PartidoDatos.Tasks();
                    nTasks.execute(Mensajes.INSERT_PAY + ";" + actual.getCoste());

                } else if (requestCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Pago Cancelado", Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){

            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(getContext(),"Pago Invalido",Toast.LENGTH_SHORT).show();

        }
    }
}