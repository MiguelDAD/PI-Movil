package com.example.pi_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Torneo;
import com.ventura.bracketslib.BracketsView;
import com.ventura.bracketslib.model.ColomnData;
import com.ventura.bracketslib.model.CompetitorData;
import com.ventura.bracketslib.model.MatchData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DatosTorneoRondasActivity extends AppCompatActivity {

    private BracketsView bracketsView;

    private int idTorneo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_torneo_rondas);

        bracketsView = findViewById(R.id.bracket_view2);

        Bundle objetosEnviados = getIntent().getExtras();
        if (objetosEnviados != null) {
            idTorneo = objetosEnviados.getInt("id");
            ArrayList<ColomnData> colomnData = (ArrayList<ColomnData>) objetosEnviados.getSerializable("columnas");

            bracketsView.setBracketsData(colomnData);
        }

    }

    private void bracketPrueba(){

        /*CompetitorData brazilSemiFinal = new CompetitorData("Brazil", "3");
        CompetitorData englandSemiFinal = new CompetitorData("England", "1");
        CompetitorData argentinaSemiFinal = new CompetitorData("Argentina", "3");
        CompetitorData russiaSemiFinal = new CompetitorData("Russia", "2");
        CompetitorData brazilFinal = new CompetitorData("Brazil", "4");
        CompetitorData argentinaFinal = new CompetitorData("Argentina", "2");

        MatchData match1SemiFinal = new MatchData(brazilSemiFinal, englandSemiFinal);
        MatchData match2SemiFinal = new MatchData(argentinaSemiFinal, russiaSemiFinal);
        MatchData match3Final = new MatchData(brazilFinal, argentinaFinal);

        ColomnData semiFinalColomn = new ColomnData(Arrays.asList(match1SemiFinal, match2SemiFinal));
        ColomnData finalColomn = new ColomnData(Arrays.asList(match3Final));

        bracketsView.setBracketsData(Arrays.asList(semiFinalColomn, finalColomn));*/
    }


}