package com.example.pi_movil;

import static android.app.Activity.RESULT_OK;
import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adevinta.leku.LocationPickerActivity;
import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.geocode.DecimalDigitsInputFilter;
import com.example.pi_movil.geocode.Direccion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;


public class PartidoCrear extends Fragment {

    private final static int PLACE_PICKER_REQUEST = 999;

    Spinner deportes;
    TextView ubicacion, fInicio, hInicio, fFin, hFin;
    EditText coste;
    Button crearPartido;

    Direccion direccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_partido_crear, container, false);

        deportes = view.findViewById(R.id.spinnerPartidoDeportes);
        cargarDeportes();

        direccion = null;
        ubicacion = view.findViewById(R.id.datoPartidoUbicacion);
        fInicio = view.findViewById(R.id.datoPartidofInicio);
        hInicio = view.findViewById(R.id.datoPartidohInicio);
        fFin = view.findViewById(R.id.datoPartidofLimite);
        hFin = view.findViewById(R.id.datoPartidohLimite);
        coste = view.findViewById(R.id.datoPartidoCoste);

        coste.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});


        crearPartido = view.findViewById(R.id.datoPartidoCrear);

        cargarClicks();

        return view;
    }

    private void cargarClicks(){
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new LocationPickerActivity.Builder()
                        .withGooglePlacesApiKey("AIzaSyDszvn-7Y3ucW7nxCjF6ueSwZdjsj3vzj0")
                        .build(getContext());


                startActivityForResult(intent, PLACE_PICKER_REQUEST);

            }
        });

        fInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate fechaActual = LocalDate.now();
                DatePickerDialog d = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                            fInicio.setText(year+"/"+String.format("%2s", (month+1)+"").replace(' ', '0')
                                    +"/"+String.format("%2s", day+"").replace(' ', '0'));

                    }
                },fechaActual.getYear(),fechaActual.getMonthValue()-1,fechaActual.getDayOfMonth());
                d.show();
            }
        });

        fFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate fechaActual = LocalDate.now();
                DatePickerDialog d = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        fFin.setText(year+"/"+String.format("%2s", (month+1)+"").replace(' ', '0')
                                +"/"+String.format("%2s", day+"").replace(' ', '0'));

                    }
                },fechaActual.getYear(),fechaActual.getMonthValue()-1,fechaActual.getDayOfMonth());
                d.show();
            }
        });

        hInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog d = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        hInicio.setText(String.format("%2s", hour+"").replace(' ', '0')
                                +":"+String.format("%2s", minute+"").replace(' ', '0'));
                    }
                },0,0,true);
                d.show();
            }
        });

        hFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog d = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        hFin.setText(String.format("%2s", hour+"").replace(' ', '0')
                                +":"+String.format("%2s", minute+"").replace(' ', '0'));
                    }
                },0,0,true);
                d.show();
            }
        });

        crearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPartido();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Log.d("RESULT****", "OK");
            double latitude = data.getDoubleExtra(LATITUDE, 0.0);
            Log.d("LATITUDE****", latitude+"");
            double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
            Log.d("LONGITUDE****", longitude+"");

            direccion = new Direccion(getContext(),latitude,longitude);

            ubicacion.setText(direccion.toString());

        } else {
            Log.d("RESULT****", "CANCELLED");
        }

    }

    private void cargarDeportes(){

        PartidoCrear.Tasks nTasks = new PartidoCrear.Tasks();
        nTasks.execute(Mensajes.GET_SPORT);

    }

    private void crearPartido(){

        //COMPROBAR DATOS

        String deporteSeleccionado = deportes.getItemAtPosition(deportes.getSelectedItemPosition()).toString();
        if(direccion==null){
            Toast.makeText(getContext(), "Direccion incorrecta", Toast.LENGTH_SHORT).show();
            return;
        }
        String ubicacionSeleccionado = direccion.getLatitud()+","+direccion.getLongitud();

        String fechaInicio = fInicio.getText().toString();
        String horaInicio = hInicio.getText().toString();
        String fechaFin = fFin.getText().toString();
        String horaFin = hFin.getText().toString();
        String precioText = coste.getText().toString();

        if(deporteSeleccionado.isEmpty()||fechaInicio.isEmpty()||horaInicio.isEmpty()
                ||fechaFin.isEmpty()||horaFin.isEmpty()||precioText.isEmpty()){
            Toast.makeText(getContext(), "Campos sin rellenar", Toast.LENGTH_SHORT).show();
            return;
        }

        //COMPROBAR QUE LA FECHA DE INICIO SEA SUPERIOR A LA ACTUAL
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        LocalDateTime fechaHoraActual = LocalDateTime.now();
        LocalDateTime fechaHoraInicio = LocalDateTime.parse(fechaInicio+" "+horaInicio, formato);

        if (fechaHoraInicio.isBefore(fechaHoraActual)) {
            Toast.makeText(getContext(), "La fecha del partido debe ser superior a la actual", Toast.LENGTH_SHORT).show();
            return;
        }

        //COMPROBAR QUE LA FECHA LIMITE DE INSCRIPCION SEA ANTES QUE EL PARTIDO
        LocalDateTime fechaHoraFin = LocalDateTime.parse(fechaFin+" "+horaFin, formato);
        if (fechaHoraFin.isAfter(fechaHoraInicio)) {
            Toast.makeText(getContext(), "La fecha máxima debe ser inferior a el inicio del partido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fechaHoraFin.isBefore(LocalDateTime.now())){
            Toast.makeText(getContext(), "La fecha máxima debe ser inferior a la de hoy", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i("DATOS PARTIDO", deporteSeleccionado+" "+ubicacionSeleccionado+" "+fechaInicio+" "+horaInicio
                +" "+fechaFin+" "+horaFin+" "+precioText);

        //ubicacion;fInicio;hInicio;fLimite;hLimite;coste;deporte

        String mensaje = Mensajes.GAME_CREATE+";"+ubicacionSeleccionado+";"+fechaInicio+";"+horaInicio+";"+fechaFin+";"+horaFin+";"+precioText+";"+deporteSeleccionado;

        PartidoCrear.Tasks nTasks = new PartidoCrear.Tasks();
        nTasks.execute(mensaje);
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

                Log.v("Recibido del servidor: ", recibido);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "El servidor esta offline", Toast.LENGTH_SHORT).show();
            }

            return recibido;
        }

        @Override
        public void onPostExecute(String respuesta) {

            Log.v("RESPUESTA", respuesta);
            String[] partesRespuesta = respuesta.split(";");
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.SEND_SPORT)) {

                List<String> listaDeportes = new LinkedList<>();

                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] partesDeporte = partesRespuesta[i].split(":");

                    listaDeportes.add(partesDeporte[0]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, listaDeportes);
                deportes.setAdapter(adapter);
                deportes.setSelection(0);

                ((HomeActivity)getActivity()).completado();
            } else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.ERROR_SEND_SPORT)) {

                Toast.makeText(getActivity(), "Ha ocurrido un problema con el servidor", Toast.LENGTH_SHORT).show();

                PartidosFragment nuevoFragmento = new PartidosFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();

                transaction.commit();

            } else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.GAME_CREATE_OK)){
                Toast.makeText(getActivity(), "Partido creado", Toast.LENGTH_SHORT).show();
                PartidosFragment nuevoFragmento = new PartidosFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();

                transaction.commit();


            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.GAME_CREATE_ERROR)){
                Toast.makeText(getActivity(), "No se ha podido crear el partido", Toast.LENGTH_SHORT).show();
                PartidosFragment nuevoFragmento = new PartidosFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();

                transaction.commit();
            }else{
                Toast.makeText(getActivity(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
                PartidosFragment nuevoFragmento = new PartidosFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();

                transaction.commit();
            }
        }
    }
}