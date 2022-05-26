package com.example.carapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "cars_db";
    public static final int DB_VERSION = 1;

    public static final String CAR_TB_NAME = "car";
    public static final String CAR_CLN_ID = "id";
    public static final String CAR_CLN_MODEL = "model";
    public static final String CAR_CLN_COLOR = "color";
    public static final String CAR_CLN_DESCRIPTION = "description";
    public static final String CAR_CLN_IMAGE = "image";
    public static final String CAR_CLN_DPL = "distancePerLetter";

    public MyDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //يتم استدعائها عند انشاء الداتابيز
        sqLiteDatabase.execSQL("CREATE TABLE "+CAR_TB_NAME+" ("+CAR_CLN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , " +
                CAR_CLN_MODEL+" TEXT , "+CAR_CLN_COLOR+" TEXT ,"+CAR_CLN_DESCRIPTION+" TEXT,"+CAR_CLN_IMAGE+" TEXT, "+CAR_CLN_DPL+" REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // يتم استدعئها عند كل تحديث لداتابيز
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS car");
        onCreate(sqLiteDatabase);
    }

    public boolean insertCar(Car car){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAR_CLN_MODEL,car.getModel());
        values.put(CAR_CLN_COLOR,car.getColor());
        values.put(CAR_CLN_DESCRIPTION,car.getDescription());
        values.put(CAR_CLN_IMAGE,car.getImage());
        values.put(CAR_CLN_DPL,car.getDpl());

        long result = database.insert(CAR_TB_NAME,null,values);
        return result != 1;
    }

    public boolean updateCar(Car car){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CAR_CLN_MODEL,car.getModel());
        values.put(CAR_CLN_COLOR,car.getColor());
        values.put(CAR_CLN_DESCRIPTION,car.getDescription());
        values.put(CAR_CLN_IMAGE,car.getImage());
        values.put(CAR_CLN_DPL,car.getDpl());

        String args [] = new String[]{car.getId()+""};
        long result = database.update(CAR_TB_NAME, values,CAR_CLN_ID+"=?",args);
        return result > 0;
    }

    public long getCarCount(){
        SQLiteDatabase database = getWritableDatabase();
        return DatabaseUtils.queryNumEntries(database,CAR_TB_NAME);
    }

    public boolean deleteCar(Car car){
        SQLiteDatabase database = getWritableDatabase();
        String args [] = new String[]{car.getId()+""};
        long result = database.delete(CAR_TB_NAME,CAR_CLN_ID+"=?",args);
        return result > 0;
    }

    public ArrayList<Car> getAllCar () {
        SQLiteDatabase database = getWritableDatabase();
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+CAR_TB_NAME,null);

        if(cursor.moveToFirst() && cursor != null){
            do{
                int id = cursor.getInt(0);
                String model = cursor.getString(1);
                String color = cursor.getString(2);
                String description = cursor.getString(3);
                String image = cursor.getString(4);
                double dpl = cursor.getDouble(5);
                Car car = new Car(id,model,color,description,image,dpl);
                cars.add(car);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return cars;
    }

    public ArrayList<Car> getCars (String modelSearch) {
        SQLiteDatabase database = getWritableDatabase();
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+CAR_TB_NAME+" WHERE "+CAR_CLN_MODEL+"=?",new String[]{modelSearch});

        if(cursor.moveToFirst() && cursor != null){
            do{
                int id = cursor.getInt(0);
                String model = cursor.getString(1);
                String color = cursor.getString(2);
                String description = cursor.getString(3);
                String image = cursor.getString(4);
                double dpl = cursor.getDouble(5);
                Car car = new Car(id,model,color,description,image,dpl);
                cars.add(car);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return cars;
    }

    public Car getCar (int car_id) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+CAR_TB_NAME+" WHERE "+CAR_CLN_ID+"=?",new String[]{String.valueOf(car_id)});

        if(cursor.moveToFirst() && cursor != null){
                int id = cursor.getInt(0);
                String model = cursor.getString(1);
                String color = cursor.getString(2);
                String description = cursor.getString(3);
                String image = cursor.getString(4);
                double dpl = cursor.getDouble(5);
                Car car = new Car(id,model,color,description,image,dpl);
                cursor.close();
                return car;
        }

        return null;
    }

}
