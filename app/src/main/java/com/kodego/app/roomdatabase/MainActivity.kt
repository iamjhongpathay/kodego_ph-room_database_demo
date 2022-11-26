package com.kodego.app.roomdatabase

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.app.roomdatabase.databinding.ActivityMainBinding
import com.kodego.app.roomdatabase.databinding.DialogUpdateEmployeeBinding
import com.kodego.app.roomdatabase.db.CompanyDatabase
import com.kodego.app.roomdatabase.db.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var companyDB: CompanyDatabase
    lateinit var employeeAdapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        companyDB = CompanyDatabase.invoke(this)
        //display data to recycler view
        view()

        binding.btnSave.setOnClickListener(){
            var name: String = binding.etName.text.toString()
            var salary: Int = binding.etSalary.text.toString().toInt()
            
            val employee = Employee(name, salary)
            save(employee)
            employeeAdapter.employeeModel.add(employee)
            employeeAdapter.notifyDataSetChanged()

            Toast.makeText(applicationContext, "Saved!", Toast.LENGTH_LONG).show()
        }
    }


    private fun view() {
        lateinit var employee: MutableList<Employee>
        GlobalScope.launch(Dispatchers.IO) {
            employee = companyDB.getEmployees().getAllEmployees()

            withContext(Dispatchers.Main){
                employeeAdapter = EmployeeAdapter(employee)
                //display to recyclerview
                binding.recyclerViewEmployees.adapter = employeeAdapter
                binding.recyclerViewEmployees.layoutManager = LinearLayoutManager(applicationContext)

                showDeleteDialog()

                employeeAdapter.onEmployeeUpdate =  { item:Employee, position: Int ->
                    showUpdateDialog(item.id)
                    employeeAdapter.notifyDataSetChanged()
                }

            }
        }
    }

    private fun save(employee: Employee) {
        GlobalScope.launch(Dispatchers.IO) {
            companyDB.getEmployees().addEmployee(employee)
            view()
        }
    }

    private fun delete(employee: Employee){

        GlobalScope.launch(Dispatchers.IO) {
            companyDB.getEmployees().deleteEmployee(employee.id)
            view()
        }
    }

    private fun showDeleteDialog(){
        employeeAdapter.onEmployeeDelete = { item: Employee, position: Int ->
            AlertDialog.Builder(this).setMessage("Do you want to delete this data? \n'${item.name} = ${item.salary}'")
                .setPositiveButton("Delete"){ dialog, item2 ->
                    delete(item)
                    employeeAdapter.employeeModel.removeAt(position)
                    employeeAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Successful Deleted!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel"){dialog, item ->
                }.show()
        }
    }

    private fun showUpdateDialog(id: Int){
        val dialog = Dialog(this)
        val binding: DialogUpdateEmployeeBinding = DialogUpdateEmployeeBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnUpdate.setOnClickListener(){
            var newName: String = binding.etNewName.text.toString()
            var newSalary: Int = binding.etNewSalary.text.toString().toInt()
            GlobalScope.launch(Dispatchers.IO) {
                companyDB.getEmployees().updateEmployee(id, newName, newSalary)
                view()
            }
            dialog.dismiss()
        }
    }
}

//Source: https://www.youtube.com/watch?v=ke6rCVy5XHQ
        //https://www.youtube.com/watch?v=bG01Kryhi5o