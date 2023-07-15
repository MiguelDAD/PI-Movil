package com.example.pi_movil.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.pi_movil.R;

//https://www.youtube.com/watch?v=ARezg1D9Zd0&ab_channel=CodinginFlow
public class InvitarUsuarioDialogo extends AppCompatDialogFragment {

    private EditText usuario;
    private InvitarUsuarioDialogoListener listener;

    public InvitarUsuarioDialogo(InvitarUsuarioDialogoListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_invitarusuario,null);

        usuario = view.findViewById(R.id.dialog_invitarUsu_usu);

        builder.setView(view)
                .setTitle("Invitar usuario")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String user = usuario.getText().toString();
                        listener.applyTexts(user);
                    }
                });

        return builder.create();
    }

    public interface InvitarUsuarioDialogoListener{
        void applyTexts(String usuario);
    }

}
