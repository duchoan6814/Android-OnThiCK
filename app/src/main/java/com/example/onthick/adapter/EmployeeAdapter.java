package com.example.onthick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onthick.R;
import com.example.onthick.entity.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<Employee> employees;
    private Context context;
    private OnActionEmployeeItem onActionEmployeeItem;

    public EmployeeAdapter(List<Employee> employees, Context context, OnActionEmployeeItem onActionEmployeeItem) {
        this.employees = employees;
        this.context = context;
        this.onActionEmployeeItem = onActionEmployeeItem;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View employeeView = inflater.inflate(R.layout.employee_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(employeeView, this.onActionEmployeeItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = this.employees.get(position);
        holder.txvId.setText(employee.getId());
        holder.txvName.setText(employee.getFullName());
        if (employee.isSex()) {
            holder.imvSex.setImageResource(R.drawable.ic_bussiness_man);
        } else {
            holder.imvSex.setImageResource(R.drawable.ic_woman);
        }
    }

    @Override
    public int getItemCount() {
        return this.employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvSex;
        private TextView txvId;
        private TextView txvName;
        private Button btnDelete;
        private OnActionEmployeeItem onActionEmployeeItem;

        public ViewHolder(@NonNull View itemView, OnActionEmployeeItem onActionEmployeeItem) {
            super(itemView);

            txvId = itemView.findViewById(R.id.txvEmItemId);
            txvName = itemView.findViewById(R.id.txvEmItemName);
            imvSex = itemView.findViewById(R.id.imvEmItemSex);
            btnDelete = itemView.findViewById(R.id.btnEmItemDelete);

            this.onActionEmployeeItem = onActionEmployeeItem;

            itemView.setOnClickListener(this);
            // set action for button delete
            btnDelete.setOnClickListener(v -> {
                this.onActionEmployeeItem.onClickButtonDeleteItem(getAdapterPosition());
            });

        }

        @Override
        public void onClick(View v) {
            this.onActionEmployeeItem.onClickItem(getAdapterPosition());
        }
    }

    public interface OnActionEmployeeItem {
        void onClickItem(int position);

        void onClickButtonDeleteItem(int position);
    }
}
