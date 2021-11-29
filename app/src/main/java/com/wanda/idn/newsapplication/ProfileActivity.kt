package com.wanda.idn.newsapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.wanda.idn.newsapplication.databinding.ActivityProfileBinding
import org.jetbrains.anko.support.v4.fragmentTabHost

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var profileBinding: ActivityProfileBinding


    var refUser : DatabaseReference? = null
    var firebaseUser : FirebaseUser? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)
        supportActionBar?.hide()
        profileBinding.apply {
            tvLogout.setOnClickListener(this@ProfileActivity)
            ivBackProfile.setOnClickListener(this@ProfileActivity)
        }
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUser = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        refUser!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val name = snapshot.child("Fullname").value.toString()
                    val email = snapshot.child("Email").value.toString()
                    val photo = snapshot.child("photo").value.toString()
                    profileBinding.apply {
                        tvNameProfile.text = name
                        tvEmaiProfile.text = email
                        Glide.with(this@ProfileActivity).load(photo).into(profileBinding.ivProfile)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }
    companion object{
    fun getLaunchService(from : Context) = Intent(from, ProfileActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
}

    override fun onClick(v: View) {
        when(v.id){
            R.id.tv_logout -> logOut()
        }

    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }
}