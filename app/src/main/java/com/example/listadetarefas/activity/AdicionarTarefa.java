
package com.example.listadetarefas.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.listadetarefas.R;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;

public class AdicionarTarefa extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
        setTitle("");

        editTarefa = findViewById(R.id.textTarefa);

        //Recuperar tarefa, caso seja edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //Configurar tarefa na caixa de texto
        if ( tarefaAtual !=null){
            editTarefa.setText( tarefaAtual.getNomeTarefa());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSalvar:

                // Executa ação para o item salvar

                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());


                if (tarefaAtual != null) { //edicao

                    String nomeTarefa = editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {

                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa( nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        //atualizar no banco de dados
                        if(tarefaDAO.actualizar(tarefa)){
                            finish();
                            Toast.makeText(getApplicationContext(), "Sucess updating task",Toast.LENGTH_SHORT).show();


                        }else{
                            Toast.makeText(getApplicationContext(),"Error updating task", Toast.LENGTH_SHORT).show();

                        }

                    }





                } else { //salvar


                    String nomeTarefa = editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {

                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);


                        if (tarefaDAO.salvar(tarefa) ){
                            finish();
                            Toast.makeText(getApplicationContext(),"Sucess saving task!", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getApplicationContext(),"Error saving task!", Toast.LENGTH_SHORT).show();


                        }




                    }


                    break;
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
