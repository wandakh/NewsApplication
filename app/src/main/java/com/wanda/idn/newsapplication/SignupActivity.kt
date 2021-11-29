package com.wanda.idn.newsapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wanda.idn.newsapplication.databinding.ActivitySignupBinding
import org.jetbrains.anko.coroutines.experimental.asReference

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUser: DatabaseReference
    private var firebaseId: String = ""
    private lateinit var activitiySignupBinding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitiySignupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(activitiySignupBinding.root)
        mAuth = FirebaseAuth.getInstance()
        activitiySignupBinding.btnSignup.setOnClickListener(this)

    }

    companion object {
        fun getLaunchService(from: Context) = Intent(from, SignupActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnSignup -> signupUser()
        }
    }

    private fun signupUser() {
        val fullName: String = activitiySignupBinding.etNameSignup.text.toString()
        val email: String = activitiySignupBinding.etNameSignup.text.toString()
        val password: String = activitiySignupBinding.etPasswordSignup.text.toString()
        val confirmPassword: String = activitiySignupBinding.etConfirmPasswordSignup.text.toString()

        if (fullName == "") {
            Toast.makeText(this, "Not Empty", Toast.LENGTH_SHORT).show()

        } else if (email == "") {
            Toast.makeText(this, "Not Empty", Toast.LENGTH_SHORT).show()
        } else if (password == "") {
            Toast.makeText(this, "Not Empty", Toast.LENGTH_SHORT).show()
        } else if ((email == "").equals(password)) {
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show()
        }else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCanceledListener { ->
                if (it.isSuccesful) {
                    firebaseId = mAuth.currentUser!!.uid
                    refUser = FirebaseDatabase.getInstance().reference.child("Users")
                        .child(firebaseId)
                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseId
                    userHashMap["fullname"] = fullName
                    userHashMap["email"] = email
                    userHashMap["linkedIn"] = ""
                    userHashMap["instagram"] = ""
                    userHashMap["medium"] = ""
                    userHashMap["photo"] = ""


                    refUser.updateChildren(userHashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity((Intent(MainActivity.getLaunchService(this))))
                            finish()
                        } else {
                            Toast.makeText(this, "Register Failed" + it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }


                }


            }

        }

