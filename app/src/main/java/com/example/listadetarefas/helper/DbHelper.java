package com.example.listadetarefas.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 2;
    public static String NOME_DB = "DB_TARESFAS";
    public static String TABELA_TAREFAS = "tarefas";


    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase ) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT NOT NULL); ";


        try {

            sqLiteDatabase.execSQL( sql );
            Log.i("INFO DB", "Sucesso ao criar a tabela" );

        }catch (Exception e){
            Log.i("INFO DB", "Erro ao criar tabela" + e.getMessage());

        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String sql = "DROP TABLE IF EXISTS " + TABELA_TAREFAS + " ;";


        try {

            sqLiteDatabase.execSQL( sql );
            onCreate(sqLiteDatabase);
            Log.i("INFO DB", "Sucesso ao actualizar App" );

        }catch (Exception e){
            Log.i("INFO DB", "Erro ao actualizar App" + e.getMessage());

        }



    }
}
