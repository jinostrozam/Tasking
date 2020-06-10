package com.dronery.tasking.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dronery.tasking.R;
import com.dronery.tasking.managers.FirestoreManager;
import com.dronery.tasking.model.Actividades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExampleFragment extends DefaultFragment {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listVIew;
    Button button;
    Actividades lista = new Actividades();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String nameStr;
    FloatingActionButton mail;
    AlertDialog.Builder builder;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        builder = new AlertDialog.Builder(getContext());
        listVIew = (ListView) root.findViewById(R.id.idListView);
        button = (Button) root.findViewById(R.id.btnAddItem); //new

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        items = new ArrayList<>(); //new
        itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        listVIew.setAdapter(itemsAdapter);

        setUpListViewListener();
        loadUserFirebaseData();

        return root;
    }

    private void setUpListViewListener() {
        try{

            listVIew.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Context context = getActivity().getApplicationContext();

                    nameStr = parent.getItemAtPosition(position).toString();

                    Toast.makeText(context,
                            getContext().getResources().getString(R.string.word_task) + " " + nameStr + " "
                                    + getContext().getResources().getString(R.string.word_removed), Toast.LENGTH_LONG
                    ).show();
                    items.remove(position);
                    itemsAdapter.notifyDataSetChanged();


                    lista.getTextos().remove(nameStr);
                    firestoreManager.saveObject(FirestoreManager.FS_COLLECTION_USUARIOS, userId, lista, task1 -> {
                        itemsAdapter.remove(nameStr);
                    });

                    return true;
                }
            });


        }catch(Throwable e){
            e.printStackTrace();
            builder.setTitle(R.string.alert_header).setMessage(R.string.alert_error_delete_item)
                    .setNeutralButton(R.string.alert_button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            testEmail();
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }

    public void testEmail(){
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ getContext().getResources().getString(R.string.alert_mail_dev) });
        intent.putExtra(Intent.EXTRA_SUBJECT, getContext().getResources().getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getContext().getResources().getString(R.string.email_body) +
                getContext().getResources().getString(R.string.code_email_ExampleFragment));
        intent.setType("text/plain");
        final PackageManager pm = getActivity().getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
        if (best != null)
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(intent);
    }


    public void loadUserFirebaseData() {
        try{
            firestoreManager.getDocument(FirestoreManager.FS_COLLECTION_USUARIOS, userId, task -> {
                if(!task.isSuccessful()){
                    return;
                }
                DocumentSnapshot documento = task.getResult();
                lista = documento.toObject(Actividades.class);

                if(lista == null){
                    lista = new Actividades();
                }

                if(lista.getTextos() == null){
                    lista.setTextos(new ArrayList<>());
//                lista.setEstado(new ArrayList<>());
                }

                for (int i=0; i<= lista.getTextos().size() -1; i++){
//                if(lista.getEstado().get(i) == false) {
                    itemsAdapter.add(lista.getTextos().get(i));
//                }
                }
            });
        }catch(Throwable e){
            e.printStackTrace();
            builder.setTitle(R.string.alert_header).setMessage(R.string.alert_error_get_document)
                    .setNeutralButton(R.string.alert_button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            testEmail();
                            dialog.cancel();
                        }
                    })
                    .show();
        }

    }
//
    private void addItem() {
        try{
            EditText input = getActivity().findViewById(R.id.etNewItem);
            String itemText = input.getText().toString();

            if(!(itemText.equals(""))){
                lista.getTextos().add(itemText);
                firestoreManager.saveObject(FirestoreManager.FS_COLLECTION_USUARIOS, userId, lista, task1 -> {
                    itemsAdapter.add(itemText);
                    input.setText("");
                });

                Toast.makeText(getContext(), R.string.alert_succesful_firebase,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), R.string.alert_empty_task, Toast.LENGTH_LONG).show();
            }
        }catch(Throwable e){
            e.printStackTrace();
            builder.setTitle(R.string.alert_header).setMessage(R.string.alert_error_add_item)
                    .setNeutralButton(R.string.alert_button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            testEmail();
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }

    @Override
    public View setFragmentLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void createViewItems(View root) {

    }

}