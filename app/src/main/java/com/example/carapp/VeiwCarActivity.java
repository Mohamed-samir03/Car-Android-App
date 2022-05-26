package com.example.carapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class VeiwCarActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQ_COD = 1;
    public static final int ADD_CAR_RES_COD = 2;
    public static final int EDIT_CAR_RES_COD = 3;
    private Toolbar toolbar;
    private TextInputEditText et_model,et_color,et_dpl,et_description;
    private ImageView iv;
    private MyDatabase db;
    private Uri imageUri;

    private int car_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_car);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iv = findViewById(R.id.iv_details);
        et_model = findViewById(R.id.et_details_model);
        et_color = findViewById(R.id.et_details_color);
        et_dpl = findViewById(R.id.et_details_dpl);
        et_description = findViewById(R.id.et_details_description);

        Intent intent = getIntent();
        car_id = intent.getIntExtra(MainActivity.car_key,-1);
        db = new MyDatabase(this);
        if(car_id == -1){
            //اضافة
            enableFields();
            clearFields();
        }else{
            //عرض
            disableFields();
            Car c = db.getCar(car_id);
            if(c!=null){
                fillCarToFields(c);
            }
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in,PICK_IMAGE_REQ_COD);
            }
        });

    }

    private void fillCarToFields(Car c){
        if(c.getImage()!=null && !c.getImage().equals(""))
            iv.setImageURI(Uri.parse(c.getImage()));
        et_model.setText(c.getModel());
        et_color.setText(c.getColor());
        et_dpl.setText(c.getDpl()+"");
        et_description.setText(c.getDescription());
    }

    private void disableFields(){
        iv.setEnabled(false);
        et_model.setEnabled(false);
        et_color.setEnabled(false);
        et_dpl.setEnabled(false);
        et_description.setEnabled(false);
    }
    private void enableFields(){
        iv.setEnabled(true);
        et_model.setEnabled(true);
        et_color.setEnabled(true);
        et_dpl.setEnabled(true);
        et_description.setEnabled(true);
    }
    private void clearFields(){
        iv.setImageURI(null);
        et_model.setText("");
        et_color.setText("");
        et_dpl.setText("");
        et_description.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu,menu);
        MenuItem save = menu.findItem(R.id.details_menu_sava);
        MenuItem edit = menu.findItem(R.id.details_menu_edit);
        MenuItem delete = menu.findItem(R.id.details_menu_delete);
        if(car_id == -1){
            //اضافة
            save.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
        }else{
            //عرض
            save.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String model,color,desc,image="";
        double dpl;
        switch (item.getItemId()){
            case R.id.details_menu_sava:
                model = et_model.getText().toString();
                color = et_color.getText().toString();
                desc = et_description.getText().toString();
                dpl = Double.parseDouble(et_dpl.getText().toString());
                if(imageUri != null)
                    image = imageUri.toString();
                boolean res;
                Car c = new Car(car_id,model,color,desc,image,dpl);
                if(car_id == -1){
                    res = db.insertCar(c);
                    Toast.makeText(this, "car added", Toast.LENGTH_SHORT).show();
                    setResult(ADD_CAR_RES_COD,null);
                    finish();
                }else{
                    res = db.updateCar(c);
                    Toast.makeText(this, "car modified", Toast.LENGTH_SHORT).show();
                    setResult(EDIT_CAR_RES_COD,null);
                    finish();
                }
                return true;
            case R.id.details_menu_edit:
                MenuItem save = toolbar.getMenu().findItem(R.id.details_menu_sava);
                MenuItem edit = toolbar.getMenu().findItem(R.id.details_menu_edit);
                MenuItem delete = toolbar.getMenu().findItem(R.id.details_menu_delete);
                save.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
                enableFields();
                return true;
            case R.id.details_menu_delete:
                c = new Car(car_id,null,null,null,null,0);
                res = db.deleteCar(c);
                if(res){
                    Toast.makeText(this, "car deleted", Toast.LENGTH_SHORT).show();
                    setResult(EDIT_CAR_RES_COD,null);
                    finish();
                }
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQ_COD && resultCode == RESULT_OK){
            if(data != null){
                imageUri = data.getData();
                iv.setImageURI(imageUri);
            }
        }
    }
}