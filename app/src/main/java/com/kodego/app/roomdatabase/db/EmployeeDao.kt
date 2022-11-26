package com.kodego.app.roomdatabase.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmployeeDao {
    @Insert
    fun addEmployee(employee: Employee)

    @Query("UPDATE Employee SET name = :name, salary = :salary WHERE id = :id")
    fun updateEmployee(id: Int, name: String, salary: Int )

    @Query("DELETE FROM Employee WHERE id = :id")
    fun deleteEmployee(id:Int)

    @Query("SELECT * FROM Employee")
    fun getAllEmployees():MutableList<Employee>
}