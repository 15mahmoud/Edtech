package com.example.student_project.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.student_project.data.model.User
import java.util.UUID


@Dao
interface StudentDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudent(student: User)

    @Query("SELECT * FROM student where id = :id")
    suspend fun getStudent(id: String): User

    @Query("SELECT * FROM student")
    suspend fun getAllStudents(): User

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStudent(student: User)
}
