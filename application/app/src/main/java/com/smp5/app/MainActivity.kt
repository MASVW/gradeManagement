package com.smp5.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.StudentModel
import com.smp5.app.model.SubjectModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : FragmentActivity() {
    private val TAG = "MainActivity"
    private val api by lazy { APIRetrofit().endpoint }

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var listStudent: RecyclerView
    private lateinit var fabCreateStudent: FloatingActionButton
    private lateinit var fabCreateSubject: FloatingActionButton
    private lateinit var fabCreateGrade: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView

    private val studentFragment = StudentFragment()
    private val subjectFragment = SubjectFragment()
    /*private val gradesFragment = GradesFragment()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupBottomNavigation()
        loadFragment(studentFragment)
    }

    private fun setupView(){
        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        fabCreateStudent = findViewById(R.id.createStudent)
        fabCreateSubject = findViewById(R.id.createSubject)
        fabCreateGrade = findViewById(R.id.createGrade)

        fabCreateStudent.setOnClickListener {
            startActivity(Intent(this, CreateStudentActivity::class.java))
        }
        fabCreateSubject.setOnClickListener {
            startActivity(Intent(this, CreateSubjectActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_students -> {
                    loadFragment(studentFragment)
                    fabCreateStudent.show()
                    fabCreateSubject.hide()
                    fabCreateGrade.hide()
                    true
                }
                R.id.navigation_subjects -> {
                    loadFragment(subjectFragment)
                    fabCreateStudent.hide()
                    fabCreateSubject.show()
                    fabCreateGrade.hide()
                    true
                }
                /*R.id.navigation_grades -> {
                    loadFragment(gradesFragment)
                    fabCreateStudent.hide()
                    fabCreateSubject.hide()
                    fabCreateGrade.show()
                    true
                }*/
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    public fun showDeleteConfirmationDialog(student: StudentModel) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus siswa '${student.name}'?")
            .setPositiveButton("Ya") { dialog, _ ->
                deleteStudent(student)
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    public fun showDeleteSubjectConfirmationDialog(subject: SubjectModel) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus subject '${subject.name}'?")
            .setPositiveButton("Ya") { dialog, _ ->
                deleteSubject(subject)
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    public fun deleteStudent(student: StudentModel){
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.deleteStudent(student.id).execute()
                }

                if (response.isSuccessful) {
                    Log.d(TAG, "Student berhasil dihapus: ${student.name}")
                    Toast.makeText(this@MainActivity, "Student berhasil dihapus", Toast.LENGTH_SHORT).show()
                    // Refresh data setelah delete
                    (studentFragment as? StudentFragment)?.refreshData()
                } else {
                    Log.e(TAG, "Gagal menghapus student: ${response.message()}")
                    Toast.makeText(this@MainActivity, "Gagal menghapus student", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan saat menghapus: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public fun deleteSubject(subject: SubjectModel){
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.deleteSubject(subject.id).execute()
                }

                if (response.isSuccessful) {
                    Log.d(TAG, "Student berhasil dihapus: ${subject.name}")
                    Toast.makeText(this@MainActivity, "Subject berhasil dihapus", Toast.LENGTH_SHORT).show()
                    // Refresh data setelah delete
                    (subjectFragment as? SubjectFragment)?.refreshData()
                } else {
                    Log.e(TAG, "Gagal menghapus student: ${response.message()}")
                    Toast.makeText(this@MainActivity, "Gagal menghapus subject", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan saat menghapus: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
