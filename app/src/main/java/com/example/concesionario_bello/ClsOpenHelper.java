package com.example.concesionario_bello;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class ClsOpenHelper extends SQLiteOpenHelper {

    public ClsOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TblCliente(Ident_cliente text primary key," +
        "Nom_cliente text not null,Dir_cliente text not null,Tel_cliente text " +
        "not null,Activo text default 'Si')");

        db.execSQL("create table TbLVehiculo(Placa text primary key,Modelo text " +
        "not null, Marca text not null, Valor integer not null,Activo text " +
        "default 'Si')");

        db.execSQL("create table TblFactura(Cod_factura text primary key, Fecha " +
        "text not null,Ident_cliente text not null,Activo text default 'Si', " +
        "Constraint pk_factura foreign key (Ident_cliente) references " +
        "TbLCliente (Ident_cliente))");

        db.execSQL("create table TblDetalle_Factura(Cod_factura text not null, " +
        "Placa text not null,Valor_venta integer not null,constraint pk_detalle primary key " +
        "(Cod_factura,Placa)," +
        "foreign key (Cod_factura) references TblFactura(Cod_factura),foreign key " +
        "(Placa) references TblVehiculo(Placa))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table TblDetalle_Factura");
        db.execSQL("DROP table TbLFactura");
        db.execSQL("DROP table TblCliente");
        db.execSQL("DROP table TblVehiculo");{
            onCreate(db);
        }
    }
}
