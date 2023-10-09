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

public class ClientesActivity extends AppCompatActivity {

    EditText jetidentifciacion,jetnombre,jetdireccion,jettelefono;
    CheckBox jcbactivo;
    Button jbtguardar,jbtanular,jbteliminar;
    String identificacion,nombre,direccion,telefono;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario1.db",null,1);
    long respuesta;
    boolean sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar objetos Java con objetos Xml
        jetidentifciacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetdireccion=findViewById(R.id.etdireccion);
        jettelefono=findViewById(R.id.ettelefono);
        jcbactivo=findViewById(R.id.cbactivo);
        jbtguardar=findViewById(R.id.btguardar);
        jbtanular=findViewById(R.id.btanular);
        jbteliminar=findViewById(R.id.bteliminar);
        jetidentifciacion.requestFocus();
        sw=false;
    }//fin metodo onCreate

    public void Guardar(View view){
        //Llevar el contenido de los objetos a variables
        identificacion=jetidentifciacion.getText().toString();
        nombre=jetnombre.getText().toString();
        direccion=jetdireccion.getText().toString();
        telefono=jettelefono.getText().toString();
        //Validar que digito la informacion
        if (identificacion.isEmpty() || nombre.isEmpty() || direccion.isEmpty()
        || telefono.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentifciacion.requestFocus();
        }else{
            //abrir la conexion
            SQLiteDatabase db=admin.getWritableDatabase();
            ContentValues fila=new ContentValues();
            //Llenar el contenedor
            fila.put("Ident_cliente",identificacion);
            fila.put("Nom_cliente",nombre);
            fila.put("Dir_cliente",direccion);
            fila.put("Tel_cliente",telefono);
            //Guardar registro en la base de datos
            //A traves del switche adiciono o modifico
            if (sw == false)
                respuesta=db.insert("TblCliente",null,fila);
            else{
                sw=false;
                respuesta=db.update("TblCliente",fila,"Ident_cliente='"+identificacion+"'",null);
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
        identificacion=jetidentifciacion.getText().toString();
        if (!identificacion.isEmpty()){
            //Abrir la conexion en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            //Definir el Cursor y realizar la consulta
            Cursor registro=db.rawQuery("select * from TbLCliente where Ident_cliente='"+identificacion+"'",null);
            //Verificar si encontro registros o no
            if (registro.moveToNext()){
                sw=true;
                jbtanular.setEnabled(true);
                jbteliminar.setEnabled(true);
                jetnombre.setText(registro.getString(1));
                jetdireccion.setText(registro.getString(2));
                jettelefono.setText(registro.getString(3));
                if (registro.getString(4).equals("Si"))
                    jcbactivo.setChecked(true);
                else
                    jcbactivo.setChecked(false);
            }else{
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
                jcbactivo.setChecked(true);
            }
            //Activar campos
            jetnombre.setEnabled(true);
            jetdireccion.setEnabled(true);
            jettelefono.setEnabled(true);
            jetidentifciacion.setEnabled(false);
            jbtguardar.setEnabled(true);
            jetnombre.requestFocus();

            db.close();
        }else{
            Toast.makeText(this, "Identificacion es requerida", Toast.LENGTH_SHORT).show();
            jetidentifciacion.requestFocus();
        }
    }//Fin metodo Consultar

    public void Eliminar(View view){
        SQLiteDatabase db=admin.getWritableDatabase();
        respuesta=db.delete("TblCliente","Ident_cliente='"+identificacion+"'",null);
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
        respuesta=db.update("TblCliente",fila,"Ident_cliente='"+identificacion+"'",null);
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
        jetidentifciacion.setText("");
        jetnombre.setText("");
        jetdireccion.setText("");
        jettelefono.setText("");
        jcbactivo.setChecked(false);
        jetnombre.setEnabled(false);
        jetdireccion.setEnabled(false);
        jettelefono.setEnabled(false);
        jbtguardar.setEnabled(false);
        jbtanular.setEnabled(false);
        jbteliminar.setEnabled(false);
        jetidentifciacion.setEnabled(true);
        jetidentifciacion.requestFocus();
        sw=false;
    }
}