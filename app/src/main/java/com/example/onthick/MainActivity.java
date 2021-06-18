package com.example.onthick;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onthick.adapter.EmployeeAdapter;
import com.example.onthick.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EmployeeAdapter.OnActionEmployeeItem {

    private EditText edtId;
    private EditText edtName;
    private Spinner spnSex;
    private EditText edtSalary;
    private Button btnAdd;
    private Button btnEdit;
    private Button btnFind;
    private RecyclerView rcvListEmployee;

    private List<Employee> employees;
    private List<String> sexs;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtId = findViewById(R.id.edtMainId);
        edtName = findViewById(R.id.edtMainName);
        edtSalary = findViewById(R.id.edtMainSalary);
        spnSex = findViewById(R.id.spnMainSex);
        btnAdd = findViewById(R.id.btnMainAdd);
        btnEdit = findViewById(R.id.btnMainEdit);
        btnFind = findViewById(R.id.btnMainFind);
        rcvListEmployee = findViewById(R.id.rcvMainListEmployee);
        employees = new ArrayList<>();

//        init sex in spiner
        sexs = new ArrayList<>();
        sexs.add("male");
        sexs.add("female");

        ArrayAdapter spinAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, sexs);

        spnSex.setAdapter(spinAdapter);

//        request get list employee
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://60ccdbde71b73400171f8a85.mockapi.io/employees";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            passData(response);
        }, error -> {
            Log.d("bbbbhon", "onCreate: " + error);
        });
        queue.add(request);


    }

    private void passData(String response) {
        try {
            Employee[] employeeList = objectMapper.readValue(response, Employee[].class);
            for (Employee employee : employeeList) {
                System.out.println(employee);
                this.employees.add(employee);
            }

            //        add data to recircleview
            EmployeeAdapter employeeAdapter = new EmployeeAdapter(employees, this, this::onClickItem);
            rcvListEmployee.setAdapter(employeeAdapter);
            rcvListEmployee.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickItem(int position) {
        Employee employee = this.employees.get(position);
        edtId.setText(employee.getId());
        edtName.setText(employee.getFullName());
        edtSalary.setText(Double.toString(employee.getSalary()));
        if (employee.isSex()) {
            spnSex.setSelection(0);
        } else {
            spnSex.setSelection(1);
        }

    }
}