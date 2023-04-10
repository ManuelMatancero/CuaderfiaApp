package com.finalproject.cuaderfia.dbaroom.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.finalproject.cuaderfia.dbaroom.dao.ClientesDAO;
import com.finalproject.cuaderfia.dbaroom.dao.FiadosDAO;
import com.finalproject.cuaderfia.dbaroom.dao.PagosDAO;
import com.finalproject.cuaderfia.dbaroom.entidades.Clientes;
import com.finalproject.cuaderfia.dbaroom.entidades.Fiados;
import com.finalproject.cuaderfia.dbaroom.entidades.Pagos;


@Database(entities = {Clientes.class, Fiados.class, Pagos.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;

    public abstract ClientesDAO clientesDAO();
    public abstract FiadosDAO fiadosDAO();
    public abstract PagosDAO pagosDAO();

    public static AppDatabase getInstance(Context context){
        if(INSTANCE==null){

            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "fiados.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }

        return INSTANCE;

    }


}
