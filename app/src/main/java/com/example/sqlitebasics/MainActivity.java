package com.example.sqlitebasics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_product, et_description, et_price;
    Button setBtn, readBtn, unpdateBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_product = findViewById(R.id.articleID);
        et_description = findViewById(R.id.descID);
        et_price = findViewById(R.id.priceID);
    }


    public void register(View view)
    {
        DBManagement manageDB = new DBManagement(this, "products", null, 1); // Create connection to the DBManagement class
        SQLiteDatabase database = manageDB.getWritableDatabase(); // Open database in read and write mode

        // Getting text from the different edittexts
        String product = et_product.getText().toString();
        String description = et_description.getText().toString();
        String price = et_price.getText().toString();

        // validate if the fields are filled to put it inside the database
        if(!product.isEmpty() && !description.isEmpty() && !price.isEmpty())
        {
            ContentValues values = new ContentValues();

            values.put("id", product);
            values.put("description", description);
            values.put("price", price);

            database.insert("articles", null, values);

            database.close();

            et_product.setText("");
            et_description.setText("");
            et_price.setText("");

            Toast.makeText(this, "New item added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Not allowed empty fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void readProduct(View view)
    {
        DBManagement manageDB = new DBManagement(this, "products", null, 1);
        SQLiteDatabase database = manageDB.getWritableDatabase() ;

        String code = et_product.getText().toString();

        if(!code.isEmpty())
        {
            Cursor row = database.rawQuery("select description, price from articles where id = " + code, null);

            if(row.moveToFirst())
            {
                et_description.setText(row.getString(0));
                et_price.setText(row.getString(1));
                database.close();
            }
            else if (!row.moveToFirst())
            {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Article doesn't exist", Toast.LENGTH_SHORT).show();
                database.close();
            }
        }
        else
        {
            Toast.makeText(this, "You should include he product id", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteProduct(View view)
    {
        // Object of the SQLiteOpenHelper class when is previously created the table(Database management class)
        DBManagement manageDB = new DBManagement(this, "products", null, 1);

        // This is a class is created to obtain the previous class as a readable DB
        SQLiteDatabase database = manageDB.getWritableDatabase();

        String code = et_product.getText().toString();

        if(!code.isEmpty())
        {
            int deleteAction = database.delete("articles", "id=" + code, null);
            Toast.makeText(this, "Bad deleteAction: " + deleteAction, Toast.LENGTH_SHORT).show();

            if(deleteAction == 1)
            {
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "This product doesn't exist", Toast.LENGTH_SHORT).show();
            }

            et_product.setText("");
            et_description.setText("");
            et_price.setText("");

            //Cursor cursor = database.rawQuery();
        }
        else
        {
            Toast.makeText(this, "Please type a code", Toast.LENGTH_SHORT).show();
        }
    }

    public void modifyProduct(View view)
    {
        DBManagement manageDB = new DBManagement(this, "products", null, 1);
        SQLiteDatabase database = manageDB.getWritableDatabase();

        // Getting the values from the fields
        String code = et_product.getText().toString();
        String description = et_description.getText().toString();
        String price = et_price.getText().toString();


        if(!code.isEmpty() && !description.isEmpty() && !price.isEmpty())
        {
            ContentValues values = new ContentValues();
            values.put("id", code);
            values.put("description", description);
            values.put("price", price);

            int updateAction = database.update("articles", values, "id=" + code, null);
            database.close();
            if(updateAction == 1)
            {
                Toast.makeText(this, "Product successfully modified", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "This product doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "All fields have to be filled", Toast.LENGTH_SHORT).show();
        }

    }

    public void clearId(View view)
    {
        et_product.setText("");
    }

    public void clearDescription(View view)
    {
        et_description.setText("");
    }

    public void clearPrice(View view)
    {
        et_price.setText("");
    }

}
