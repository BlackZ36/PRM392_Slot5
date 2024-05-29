package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnAdd, btnUpdate, btnRemove;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Product selectedProduct;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRemove = findViewById(R.id.btnRemove);

        productDAO = new ProductDAO(this);

        if (productDAO.getAllProducts().toArray().length != 0) {
            // Tạo và thêm 5 sản phẩm vào cơ sở dữ liệu
            Product product1 = new Product("Product 1", "Description 1", true, 10, 9.99);
            Product product2 = new Product("Product 2", "Description 2", true, 15, 19.99);

            productDAO.addProduct(product1);
            productDAO.addProduct(product2);
        }

        productList = productDAO.getAllProducts();

        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(productAdapter);

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Product product) {
                selectedProduct = product;
                btnUpdate.setVisibility(View.VISIBLE);
                btnRemove.setVisibility(View.VISIBLE);
            }
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditProductActivity.class);
            startActivity(intent);
        });

        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditProductActivity.class);
            intent.putExtra("product", selectedProduct);
            startActivity(intent);
        });

        btnRemove.setOnClickListener(v -> {
            productDAO.deleteProduct(selectedProduct.getId());
            productList.remove(selectedProduct);
            productAdapter.notifyDataSetChanged();
            btnUpdate.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách sản phẩm khi quay lại MainActivity từ AddEditProductActivity
        productList.clear();
        productList.addAll(productDAO.getAllProducts());
        productAdapter.notifyDataSetChanged();
    }
}
