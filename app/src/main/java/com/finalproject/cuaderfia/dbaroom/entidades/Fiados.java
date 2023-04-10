package com.finalproject.cuaderfia.dbaroom.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Fiados")
public class Fiados {

    @PrimaryKey(autoGenerate = true)
    int id_Fiado;

    @ColumnInfo(name= "id_Cliente")
    public int id_Cliente;

    @ColumnInfo(name= "descripcion")
    public String descripcion;

    @ColumnInfo(name= "fecha")
    public String fecha;

    @ColumnInfo(name= "monto")
    public double monto;

    public int getId_Fiado() {
        return id_Fiado;
    }

    public void setId_Fiado(int id_Fiado) {
        this.id_Fiado = id_Fiado;
    }

    public int getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(int id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
