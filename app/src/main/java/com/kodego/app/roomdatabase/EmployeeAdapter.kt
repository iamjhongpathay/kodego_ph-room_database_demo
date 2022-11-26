package com.kodego.app.roomdatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kodego.app.roomdatabase.databinding.RowEmployeeBinding
import com.kodego.app.roomdatabase.db.Employee

class EmployeeAdapter(var employeeModel: MutableList<Employee>): RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    var onEmployeeDelete : ((Employee, Int) -> Unit) ? = null
    var onEmployeeUpdate : ((Employee, Int) -> Unit) ? = null

    inner class EmployeeViewHolder(var binding: RowEmployeeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowEmployeeBinding.inflate(layoutInflater, parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = employeeModel[position].name
            tvSalary.text = employeeModel[position].salary.toString()
            btnDeleteIcon.setOnClickListener(){
                onEmployeeDelete?.invoke(employeeModel[position], position)
            }

            btnEditIcon.setOnClickListener(){
                onEmployeeUpdate?.invoke(employeeModel[position],position)
            }
        }
    }

    override fun getItemCount(): Int {
        return employeeModel.size
    }
}