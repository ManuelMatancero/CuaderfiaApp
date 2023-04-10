package com.finalproject.cuaderfia.dbaroom.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Pagos")
public class Pagos {

    @PrimaryKey(autoGenerate = true)
    int id_Pago;

    @ColumnInfo(name= "id_Cliente")
    public int id_Cliente;

    @ColumnInfo(name= "descripcion")
    public String descripcion;

    @ColumnInfo(name= "fecha")
    public String fecha;

    @ColumnInfo(name= "monto")
    public double monto;

    public int getId_Pago() {
        return id_Pago;
    }

    public void setId_Pago(int id_Pago) {
        this.id_Pago = id_Pago;
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
