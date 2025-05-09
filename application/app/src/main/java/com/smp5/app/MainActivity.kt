package com.smp5.app

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.GradeModel
import com.smp5.app.model.StudentModel
import com.smp5.app.model.SubjectModel
import com.smp5.app.ui.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : FragmentActivity() {
    private val TAG = "MainActivity"
    private val api by lazy { APIRetrofit().endpoint }

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var listStudent: RecyclerView
    private lateinit var fabCreateStudent: FloatingActionButton
    private lateinit var fabCreateSubject: FloatingActionButton
    private lateinit var fabCreateGrade: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var btnLogout: Button

    private val studentFragment = StudentFragment()
    private val subjectFragment = SubjectFragment()
    private val gradesFragment = GradesFragment()

    private lateinit var authPrefs: AuthPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authPrefs = AuthPrefs(this)

        // Check if token exists and set it in APIRetrofit
        val token = authPrefs.getToken()

        Log.d(TAG, "Token : ${token}")
        if (token == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        } else {
            // Set token for API calls
            APIRetrofit.setToken(token)
        }
        setContentView(R.layout.activity_main)

        setupBioData(token);

        setupView()
        setupBottomNavigation()
        loadFragment(studentFragment)

        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog(token)
        }
    }

    private fun setupBioData(token: String){
        val parts = token.split(".")
        val payload = String(Base64.decode(parts[1], Base64.DEFAULT))
        val jsonObject = JSONObject(payload)
        val userObject = jsonObject.getJSONObject("user")
        Log.d(TAG, "${jsonObject.toString()}")
        val name = userObject.getString("name")
        val email = userObject.getString("email")

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = findViewById<TextView>(R.id.tvUserEmail)

        tvUserName.text = name
        tvUserEmail.text = email
    }

    private fun setupView(){
        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        fabCreateStudent = findViewById(R.id.createStudent)
        fabCreateSubject = findViewById(R.id.createSubject)
        fabCreateGrade = findViewById(R.id.createGrade)
        btnLogout = findViewById(R.id.btnLogout)

        fabCreateStudent.setOnClickListener {
            startActivity(Intent(this, CreateStudentActivity::class.java))
        }
        fabCreateSubject.setOnClickListener {
            startActivity(Intent(this, CreateSubjectActivity::class.java))
        }
        fabCreateGrade.setOnClickListener {
            startActivity(Intent(this, CreateGradeActivity::class.java))
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
                R.id.navigation_grades -> {
                    loadFragment(gradesFragment)
                    fabCreateStudent.hide()
                    fabCreateSubject.hide()
                    fabCreateGrade.show()
                    true
                }
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

    fun showDeleteStudentConfirmationDialog(student: StudentModel) {
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

    fun showDeleteSubjectConfirmationDialog(subject: SubjectModel) {
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

    fun showDeleteGradeConfirmationDialog(grade: GradeModel) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus grade '${grade.student.name}'?")
            .setPositiveButton("Ya") { dialog, _ ->
                deleteGrade(grade)
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    fun deleteStudent(student: StudentModel){
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

    fun showLogoutConfirmationDialog(token: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin Logout ?")
            .setPositiveButton("Ya") { dialog, _ ->
                logout()
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }


    fun deleteSubject(subject: SubjectModel){
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.deleteSubject(subject.id).execute()
                }

                if (response.isSuccessful) {
                    Log.d(TAG, "Subject berhasil dihapus: ${subject.name}")
                    Toast.makeText(this@MainActivity, "Subject berhasil dihapus", Toast.LENGTH_SHORT).show()
                    // Refresh data setelah delete
                    (subjectFragment as? SubjectFragment)?.refreshData()
                } else {
                    Log.e(TAG, "Gagal menghapus subject: ${response.message()}")
                    Toast.makeText(this@MainActivity, "Gagal menghapus subject", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan saat menghapus: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteGrade(grade: GradeModel){
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.deleteGrades(grade.id).execute()
                }

                if (response.isSuccessful) {
                    Log.d(TAG, "Grade berhasil dihapus: ${grade.student.name} ${grade.subject.name} dengan nilai ${grade.score}")
                    Toast.makeText(this@MainActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    // Refresh data setelah delete
                    (gradesFragment as? GradesFragment)?.refreshData()
                } else {
                    Log.e(TAG, "Gagal menghapus grade: ${response.message()}")
                    Toast.makeText(this@MainActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan saat menghapus: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Add a logout function
    fun logout() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.logout().execute()
                }

                // Clear token regardless of response success
                APIRetrofit.clearToken()
                authPrefs.clearToken()

                // Navigate to login screen
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()

            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan saat logout: ${e.message}", e)

                // Even on error, clear token and navigate to login
                APIRetrofit.clearToken()
                authPrefs.clearToken()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}