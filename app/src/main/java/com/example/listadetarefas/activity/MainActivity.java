package com.example.listadetarefas.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.listadetarefas.R;
import com.example.listadetarefas.activity.AdicionarTarefa;
import com.example.listadetarefas.adapter.TarefaAdapter;
import com.example.listadetarefas.helper.DbHelper;
import com.example.listadetarefas.helper.RecyclerItemClickListener;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("To do List");

        //Configurar recycler
        recyclerView = findViewById(R.id.recyclerView);

        DbHelper db = new DbHelper(getApplicationContext());


        final ContentValues cv = new ContentValues();


        db.getWritableDatabase().insert("tarefas", null, cv );

        //Adiconar evento de clique

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Recuperar tarefa para edicao
                        Tarefa tarefaSelecionada = listaTarefas.get( position );

                        //Envia tarefa para tela adicionar tarefa
                        Intent intent = new Intent(MainActivity.this, AdicionarTarefa.class);
                        intent.putExtra("tarefaSelecionada", tarefaSelecionada);

                        startActivity(intent);



                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        //Recupera tarefa para eliminar
                        tarefaSelecionada = listaTarefas.get(position);


                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                        //Configurar título e mensagem
                        dialog.setTitle("Confirmation");
                        dialog.setMessage("Would you like to delete: " + tarefaSelecionada.getNomeTarefa() + " ?");

                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                    TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                    if (tarefaDAO.deletar(tarefaSelecionada)){

                                        carregarListaTarefas();
                                        Toast.makeText(getApplicationContext(), "Task Deleted",Toast.LENGTH_SHORT).show();


                                    }else{
                                        Toast.makeText(getApplicationContext(), "Error Deleting Task",Toast.LENGTH_SHORT).show();

                                    }


                            }
                        });

                        dialog.setNegativeButton("Não", null);

                        //Exibir dialog
                        dialog.create();
                        dialog.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
                )
        );


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getApplicationContext(), AdicionarTarefa.class);
              startActivity(intent );
            }
        });
    }

    public void carregarListaTarefas(){

        //Listar tarefas

        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();




        /* Exibe lista de tarefasno RecyclerView */




        //Configurar um adapter
        tarefaAdapter = new TarefaAdapter( listaTarefas );

        //Configurar o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager( layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);


    }


    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exitButton) {


            alertDialogo();


        }

        return super.onOptionsItemSelected(item);

    }

    public void alertDialogo(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //Alert Dialog Configuration
        dialog.setTitle("Exit");
        dialog.setMessage("Are you sure you want to close?");
        dialog.setCancelable(false);

        dialog.setIcon(android.R.drawable.ic_dialog_info);



        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.create();
        dialog.show();

    }
}
