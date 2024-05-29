package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditProductActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText etName, etDescription, etQuantity, etPrice;
    private Switch switchStatus;
    private Button btnSave;
    private ProductDAO productDAO;
    private Product product;
    private boolean isEditMode;
    private Product productToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        switchStatus = findViewById(R.id.switchStatus);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        btnSave = findViewById(R.id.btnSave);


        productDAO = new ProductDAO(this);

        Intent intent = getIntent();
        if (intent.hasExtra("product")) {

            isEditMode = true;
            Product product = getIntent().getParcelableExtra("product");
            etName.setText(product.getName());
            etDescription.setText(product.getDescription());
            switchStatus.setChecked(product.isStatus());
            etQuantity.setText(String.valueOf(product.getQuantity()));
            etPrice.setText(String.valueOf(product.getPrice()));
        } else {
            isEditMode = false;
            product = new Product( "", "", false, 0, 0.0);
        }

        btnSave.setOnClickListener(v -> {
            product.setName(etName.getText().toString());
            product.setDescription(etDescription.getText().toString());
            product.setStatus(switchStatus.isChecked());
            product.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
            product.setPrice(Double.parseDouble(etPrice.getText().toString()));

            if (isEditMode) {
                productDAO.updateProduct(product);
            } else {
                productDAO.addProduct(product);
            }
            finish();
        });
    }
}
