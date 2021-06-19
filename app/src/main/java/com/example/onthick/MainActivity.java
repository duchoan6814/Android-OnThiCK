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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onthick.adapter.EmployeeAdapter;
import com.example.onthick.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
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

//        add action for button add
        btnAdd.setOnClickListener(v -> {
            try {
                handleOnclickButtonAdd();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                Toast.makeText(this, "Some error when process adding employee.",
                        Toast.LENGTH_SHORT).show();
            }
        });

//        add action for button edit
        btnEdit.setOnClickListener(v -> {
            try {
                handleOnclickButtonEdit();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                Toast.makeText(this, "Some thing wrong when process update employee.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // add action for button find
        btnFind.setOnClickListener(v -> handleOnclickButtonFind());


//        init sex in spiner
        sexs = new ArrayList<>();
        sexs.add("male");
        sexs.add("female");

        ArrayAdapter spinAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, sexs);

        spnSex.setAdapter(spinAdapter);

//        request get list employee
        loadListEmployeeToRecycirleview();

    }

    private void handleOnclickButtonFind() {

        if (edtId.getText().equals("")) {
            loadListEmployeeToRecycirleview();
        } else {
            String url = "https://60ccdbde71b73400171f8a85.mockapi.io/employees/" + edtId.getText();
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                handleWhenFindSuccess(response);
            }, error -> {
                Toast.makeText(this, "Some error when process edit employee. " + error,
                        Toast.LENGTH_SHORT).show();
            });

            queue.add(stringRequest);
        }


    }

    private void handleWhenFindSuccess(String response) {
        clearListEmployee();
        try {
            passData(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void handleOnclickButtonEdit() throws JsonProcessingException {
        // create Employ with data get some field in form
        Employee employee = new Employee();
        employee.setFullName(edtName.getText().toString());
        employee.setSex(spnSex.getSelectedItemPosition() == 0 ? true : false);
        employee.setSalary(Double.parseDouble(edtSalary.getText().toString()));

        // parse java object to json
        String jsonObjectString = objectMapper.writeValueAsString(employee);

        // call api put employee
        String url = "https://60ccdbde71b73400171f8a85.mockapi.io/employees/" + edtId.getText();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, response -> {
            handleActionWhenPutRequestCuccess();
        }, error -> {
            Toast.makeText(this, "Some error when process edit employee. " + error,
                    Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonObjectString == null ? null : jsonObjectString.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        queue.add(stringRequest);

    }

    private void handleActionWhenPutRequestCuccess() {
        Toast.makeText(this, "update success employee",
                Toast.LENGTH_SHORT).show();
        clearListEmployee();
        loadListEmployeeToRecycirleview();
    }

    private void handleOnclickButtonAdd() throws JsonProcessingException {
//        create Employ with data get some field in form
        Employee employee = new Employee();
        employee.setFullName(edtName.getText().toString());
        employee.setSex(spnSex.getSelectedItemPosition() == 0 ? true : false);
        employee.setSalary(Double.parseDouble(edtSalary.getText().toString()));

//        parse java object to json
        String jsonObjectString = objectMapper.writeValueAsString(employee);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://60ccdbde71b73400171f8a85.mockapi.io/employees";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            handleWhenAddSuccess();
        }, error -> {
            Log.e("Employee", "have an error" + error);
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonObjectString == null ? null : jsonObjectString.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        queue.add(stringRequest);

    }

    private void handleWhenAddSuccess() {
        Toast.makeText(this, "Add employee success.",
                Toast.LENGTH_SHORT).show();
        clearListEmployee();
        loadListEmployeeToRecycirleview();
    }

    public void clearListEmployee() {
        employees.clear();
    }

    private void loadListEmployeeToRecycirleview() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://60ccdbde71b73400171f8a85.mockapi.io/employees";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            try {
                passData(response);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

            }
        }, error -> {
            Log.d("bbbbhon", "onCreate: " + error);
        });
        queue.add(request);
    }

    private void passData(String response) throws JsonProcessingException {

        try {
            Employee[] employeeList = objectMapper.readValue(response, Employee[].class);
            for (Employee employee : employeeList) {
                System.out.println(employee);
                this.employees.add(employee);
            }
        } catch (Exception e) {
            Employee employee = objectMapper.readValue(response, Employee.class);
            employees.add(employee);
        }


        //        add data to recircleview
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(employees, this, this);
        rcvListEmployee.setAdapter(employeeAdapter);
        rcvListEmployee.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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

    @Override
    public void onClickButtonDeleteItem(int position) {
        String id = employees.get(position).getId();

        String url = "https://60ccdbde71b73400171f8a85.mockapi.io/employees/" + id;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, response -> {
            Toast.makeText(this, "Delete cuccess employee",
                    Toast.LENGTH_SHORT).show();
            clearListEmployee();
            loadListEmployeeToRecycirleview();
        }, error -> {
            Toast.makeText(this, "Some error when process edit employee. " + error,
                    Toast.LENGTH_SHORT).show();
        });

        queue.add(stringRequest);
    }
}