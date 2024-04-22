package com.example.pr102;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText cat_id=findViewById(R.id.cat_id);
        EditText cat_name=findViewById(R.id.cat_name);
        EditText cat_age = findViewById(R.id.cat_age);
        EditText cat_color = findViewById(R.id.cat_eyescolor);
        EditText cat_breed = findViewById(R.id.cat_breed);
        Button save_btn = findViewById(R.id.save_btn);
        Button upd_btn = findViewById(R.id.update_btn);
        Button del_btn = findViewById(R.id.del_btn);
        Button find_btn = findViewById(R.id.find_btn);
        TextView cat_info_text = findViewById(R.id.cat_info_text);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        save_btn.setOnClickListener(view -> {
            String name = cat_name.getText().toString().trim();
            String breed = cat_breed.getText().toString().trim();
            String age = cat_age.getText().toString().trim();
            String color = cat_color.getText().toString().trim();

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, name);
            values.put(DatabaseHelper.COLUMN_BREED, breed);
            values.put(DatabaseHelper.COLUMN_AGE, age);
            values.put(DatabaseHelper.COLUMN_COLOR, color);

            long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);
            if (newRowId != -1) {
                // Успешно сохранено
                Toast.makeText(MainActivity.this, "Cat saved with ID: " + newRowId, Toast.LENGTH_SHORT).show();

                // Выполняем запрос для получения данных после сохранения
                String[] projection = {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_BREED, DatabaseHelper.COLUMN_AGE, DatabaseHelper.COLUMN_COLOR};
                String selection = DatabaseHelper.COLUMN_ID + " = ?";
                String[] selectionArgs = {String.valueOf(newRowId)};

                Cursor cursor = db.query(
                        DatabaseHelper.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String catName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                    String catBreed = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BREED));
                    int catAge = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
                    String catColor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COLOR));

                    // Формируем текст для отображения найденных данных
                    String catInfo = "Name: " + catName + "\nBreed: " + catBreed + "\nAge: " + catAge + "\nColor: " + catColor;

                    // Устанавливаем этот текст в TextView
                    cat_info_text.setText(catInfo);
                }

                cursor.close();
            } else {
                // Ошибка при сохранении
                Toast.makeText(MainActivity.this, "Error saving cat", Toast.LENGTH_SHORT).show();
            }

            db.close(); // Важно закрыть базу данных после использования
        });


        upd_btn.setOnClickListener(view -> {
            String idStr = cat_id.getText().toString().trim();
            if (!idStr.isEmpty()) {
                long id = Long.parseLong(idStr);
                String name = cat_name.getText().toString().trim();
                String breed = cat_breed.getText().toString().trim();
                String age = cat_age.getText().toString().trim();
                String color = cat_color.getText().toString().trim();

                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAME, name);
                values.put(DatabaseHelper.COLUMN_BREED, breed);
                values.put(DatabaseHelper.COLUMN_AGE, age);
                values.put(DatabaseHelper.COLUMN_COLOR, color);

                int rowsUpdated = db.update(
                        DatabaseHelper.TABLE_NAME,
                        values,
                        DatabaseHelper.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)}
                );

                if (rowsUpdated > 0) {
                    // Успешно обновлено
                    Toast.makeText(MainActivity.this, "Cat updated with ID: " + id, Toast.LENGTH_SHORT).show();

                    // После обновления записи, отображаем обновленные данные
                    String[] projection = {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_BREED, DatabaseHelper.COLUMN_AGE, DatabaseHelper.COLUMN_COLOR};
                    String selection = DatabaseHelper.COLUMN_ID + " = ?";
                    String[] selectionArgs = {String.valueOf(id)};
                    Cursor cursor = db.query(
                            DatabaseHelper.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null
                    );

                    if (cursor.moveToFirst()) {
                        String catName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                        String catBreed = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BREED));
                        int catAge = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
                        String catColor = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COLOR));

                        // Формируем текст для отображения обновленных данных
                        String catInfo = "Name: " + catName + "\nBreed: " + catBreed + "\nAge: " + catAge + "\nColor: " + catColor;

                        // Устанавливаем этот текст в TextView
                        cat_info_text.setText(catInfo);
                    }

                    cursor.close();
                } else {
                    // Не удалось обновить запись (возможно, запись с таким id не существует)
                    Toast.makeText(MainActivity.this, "No cat found with ID: " + id, Toast.LENGTH_SHORT).show();
                }

                db.close(); // Важно закрыть базу данных после использования
            } else {
                // Поле ввода id пусто
                Toast.makeText(MainActivity.this, "Please enter the cat ID", Toast.LENGTH_SHORT).show();
            }
        });


        del_btn.setOnClickListener(view -> {
            // Удаление данных из базы данных
            String catid= cat_id.getText().toString().trim();
            long id = Long.parseLong(catid);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)});
            Toast.makeText(MainActivity.this, "Cat deleted with ID: " + id, Toast.LENGTH_SHORT).show();
            db.close();
        });

        find_btn.setOnClickListener(view -> {
            // Поиск данных в базе данных
            String catid= cat_id.getText().toString().trim();
            long id = Long.parseLong(catid);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            String[] projection = {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_BREED, DatabaseHelper.COLUMN_AGE, DatabaseHelper.COLUMN_COLOR};
            String selection = DatabaseHelper.COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            Cursor cursor = db.query(
                    DatabaseHelper.TABLE_NAME,   // Таблица для запроса
                    projection,                 // Столбцы, которые должны быть в результате
                    selection,                  // Условие WHERE для выборки строк
                    selectionArgs,              // Значения для условия WHERE
                    null,                       // GroupBy
                    null,                       // Having
                    null                        // OrderBy
            );
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String breed = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BREED));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COLOR));

                // Формируем текст для отображения найденных данных
                String catInfo = "Name: " + name + "\nBreed: " + breed + "\nAge: " + age + "\nColor: " + color;

                // Устанавливаем этот текст в TextView
                cat_info_text.setText(catInfo);
            } else
            cursor.close();
            db.close();
        });

    }
}