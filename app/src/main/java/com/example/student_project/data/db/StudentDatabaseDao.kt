package com.example.student_project.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.student_project.data.model.User

@Dao
interface StudentDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun addStudent(student: User)

    @Query("SELECT * FROM student where id = :id") suspend fun getStudent(id: String): User

    @Query("SELECT * FROM student Limit 1") suspend fun getCurrentStudent(): User?

    @Update(onConflict = OnConflictStrategy.REPLACE) suspend fun updateStudent(student: User)
}
