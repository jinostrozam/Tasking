package com.dronery.tasking.fragments;

import android.content.Context;
import android.os.Bundle;
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

public class ExampleFragment extends DefaultFragment {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listVIew;
    Button button;
    Actividades lista = new Actividades();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String nameStr;
    FloatingActionButton mail;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

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
        listVIew.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity().getApplicationContext();

                nameStr = parent.getItemAtPosition(position).toString();

                Toast.makeText(context,R.string.word_task + " " + nameStr + " " + R.string.word_removed, Toast.LENGTH_LONG).show();
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();


                lista.getTextos().remove(nameStr);
                firestoreManager.saveObject(FirestoreManager.FS_COLLECTION_USUARIOS, userId, lista, task1 -> {
                        itemsAdapter.remove(nameStr);
                });

                return true;
            }
        });
    }


    public void loadUserFirebaseData() {
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
    }

    private void addItem() {
        EditText input = getActivity().findViewById(R.id.etNewItem);
        String itemText = input.getText().toString();


        if(!(itemText.equals(""))){
            lista.getTextos().add(itemText);
            firestoreManager.saveObject(FirestoreManager.FS_COLLECTION_USUARIOS, userId, lista, task1 -> {
                itemsAdapter.add(itemText);
                input.setText("");
            });
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), R.string.message_enter_task, Toast.LENGTH_LONG).show();
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