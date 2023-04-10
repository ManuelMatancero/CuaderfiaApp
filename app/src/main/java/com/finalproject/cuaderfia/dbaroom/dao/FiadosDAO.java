package com.finalproject.cuaderfia.dbaroom.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.finalproject.cuaderfia.dbaroom.entidades.Fiados;

import java.util.List;



@Dao
public interface FiadosDAO {

    @Insert
    void insert (Fiados fiado);

    @Delete
    void delete (Fiados fiado);

    @Query("SELECT * FROM Fiados WHERE id_Cliente = :idCliente")
    List<Fiados> getFiados(int idCliente);


    @Query("SELECT SUM(monto) as total FROM Fiados")
    double getDeudaTotal();

    @Query("SELECT SUM(monto) as total FROM Fiados WHERE id_Cliente = :idCliente")
    double getDeudaCliente(int idCliente);

    @Query("DELETE FROM Fiados WHERE id_Cliente = :id")
    void deleteFiados(int id);

    @Query("DELETE FROM Fiados")
    void deleteAllData();











}


