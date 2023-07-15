package com.example.pi_movil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pi_movil.datos.Liga;
import com.example.pi_movil.datos.Torneo;
import com.google.android.material.tabs.TabLayout;


public class InformacionLigaFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    Liga actual;

    public InformacionLigaFragment(Liga t) {
        this.actual = t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_informacion_liga, container, false);

        addFragment(view);

        return view;
    }

    private void addFragment(View view){

        tabLayout = view.findViewById(R.id.tabLayoutLiga);
        viewPager = view.findViewById(R.id.viewPagerLiga);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //AÑADIR FRAGMENTOS

        adapter.addFragment(new DatosLigaFragment(actual),"DATOS");
        adapter.addFragment(new DatosLigaEquiposFragment(actual),"EQUIPO");
        adapter.addFragment(new DatosLigaJornadaFragment(actual),"JORNADA");
        adapter.addFragment(new DatosLigaClasificacionFragment(actual),"SCORE");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getItem(position);

                if (fragment instanceof DatosLigaEquiposFragment) {
                    ((DatosLigaEquiposFragment) fragment).obtenerEquipos();
                }else if (fragment instanceof DatosLigaJornadaFragment) {
                    ((DatosLigaJornadaFragment) fragment).cargarJornadas();
                }else if (fragment instanceof DatosLigaClasificacionFragment) {
                    ((DatosLigaClasificacionFragment) fragment).rellenarTabla();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}