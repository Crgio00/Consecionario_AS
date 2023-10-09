package com.example.concesionario_bello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class VehiculosActivity extends AppCompatActivity {

    EditText jetplaca,jetmodelo,jetmarca,jetvalor;
    CheckBox jcbactivo;
    Button jbtguardar,jbtanular,jbteliminar;
    String placa, modelo, marca, valor;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario1.db",null,1);
    long respuesta;
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar objetos Java con objetos Xml
        jetplaca=findViewById(R.id.etplaca);
        jetmodelo=findViewById(R.id.etmodelo);
        jetmarca=findViewById(R.id.etmarca);
        jetvalor=findViewById(R.id.etvalor);
        jcbactivo=findViewById(R.id.cbactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        jbteliminar=findViewById(R.id.bteliminar);
        jetplaca.requestFocus();
        sw=false;
    }
    public void Guardar(View view){
        //Llevar el contenido de los objetos a variables
        placa=jetplaca.getText().toString();
        modelo=jetmodelo.getText().toString();
        marca=jetmarca.getText().toString();
        valor=jetvalor.getText().toString();
        //Validar que digito la informacion
        if (placa.isEmpty() || modelo.isEmpty() || marca.isEmpty()
                || valor.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }else{
            //abrir la conexion
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            //Llenar el contenedor
            fila.put("Placa", placa);
            fila.put("Modelo", modelo);
            fila.put("Marca", marca);
            fila.put("Valor", valor);
            //Guardar registro en la base de datos
            //A traves del switche adiciono o modifico
            if (sw == false)
                respuesta=db.insert("TbLVehiculo",null,fila);
            else{
                sw=false;
                respuesta=db.update("TbLVehiculo",fila,"Placa='"+placa+"'",null);
            }
            //Verificar si se guardo o no
            if (respuesta > 0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }else{
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }//Fin Metodo Guardar

    public void Consultar(View view){
        //Validar que el campo identificacion no esta vacio
        placa=jetplaca.getText().toString();
        if (!placa.isEmpty()){
            //Abrir la conexion en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            //Definir el Cursor y realizar la consulta
            Cursor registro=db.rawQuery("select * from TbLVehiculo where Placa='"+placa+"'",null);
            //Verificar si encontro registros o no
            if (registro.moveToNext()){
                sw=true;
                jbtanular.setEnabled(true);
                jbteliminar.setEnabled(true);
                jetmodelo.setText(registro.getString(1));
                jetmarca.setText(registro.getString(2));
                jetvalor.setText(registro.getString(3));
                if (registro.getString(4).equals("Si"))
                    jcbactivo.setChecked(true);
                else
                    jcbactivo.setChecked(false);
            }else{
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
                jcbactivo.setChecked(true);
            }
            //Activar campos
            jetplaca.setEnabled(true);
            jetmodelo.setEnabled(true);
            jetmarca.setEnabled(true);
            jetplaca.setEnabled(false);
            jbtguardar.setEnabled(true);
            jetmodelo.requestFocus();

            db.close();
        }else{
            Toast.makeText(this, "Placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
    }//Fin metodo Consultar

    public void Eliminar(View view){
        SQLiteDatabase db=admin.getWritableDatabase();
        respuesta=db.delete("TbLVehiculo","Placa='"+placa+"'",null);
        if (respuesta > 0){
            Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
            Limpiar_campos();
        }else{
            Toast.makeText(this, "Error elimindo registro", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }//Fin metodo Eliminar

    public void Cambiar_estado(View view){
        //Abrir conexion
        SQLiteDatabase db=admin.getWritableDatabase();
        ContentValues fila=new ContentValues();
        if (jcbactivo.isChecked())
            fila.put("Activo","No");
        else
            fila.put("Activo","Si");
        //Modificar el estado
        respuesta=db.update("TbLVehiculo",fila,"Placa='"+placa+"'",null);
        if (respuesta > 0){
            Toast.makeText(this, "Estado de registro cambiado", Toast.LENGTH_SHORT).show();
            Limpiar_campos();
        }else{
            Toast.makeText(this, "Error cambiando estado", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }//Fin metodo Cambiar_estado

    public void Cancelar(View view){
        Limpiar_campos();
    }//fin metodo Cancelar

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }//fin metodo regresar

    private void Limpiar_campos(){
        jetplaca.setText("");
        jetmodelo.setText("");
        jetmarca.setText("");
        jetvalor.setText("");
        jcbactivo.setChecked(false);
        jetmodelo.setEnabled(false);
        jetmarca.setEnabled(false);
        jetvalor.setEnabled(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        jbteliminar.setEnabled(false);
        jetplaca.setEnabled(true);
        jetplaca.requestFocus();
        sw=false;
    }
}