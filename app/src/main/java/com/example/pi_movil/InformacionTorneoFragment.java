package com.example.pi_movil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pi_movil.datos.Torneo;
import com.google.android.material.tabs.TabLayout;


public class InformacionTorneoFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    Torneo actual;

    public InformacionTorneoFragment(Torneo t) {
        this.actual = t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacion_torneo, container, false);

        addFragment(view);


        return view;
    }

    private void addFragment(View view){

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //AÑADIR FRAGMENTOS

        adapter.addFragment(new DatosTorneoFragment(actual),"DATOS");
        adapter.addFragment(new DatosTorneoEquiposFragment(actual),"EQUIPOS");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getItem(position);

                if (fragment instanceof DatosTorneoEquiposFragment) {
                    ((DatosTorneoEquiposFragment) fragment).obtenerEquipos();
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