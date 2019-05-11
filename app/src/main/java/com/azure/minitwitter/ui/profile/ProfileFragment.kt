package com.azure.minitwitter.ui.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.azure.minitwitter.R
import com.azure.minitwitter.common.Constants
import com.azure.minitwitter.data.ProfileViewModel
import com.azure.minitwitter.retrofit.request.RequestUserProfile
import com.azure.minitwitter.retrofit.response.ResponseUserProfile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.profile_fragment.*
import android.Manifest

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var allPermissionsListener: PermissionListener
    private var loadingData: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.profile_fragment, container, false)

        imageViewAvatar.setOnClickListener {
            checkPermissions()
        }

        buttonSave.setOnClickListener {
            val username = editTextUsername.text.toString()
            val email = editTextEmail.text.toString()
            val description = editTextDescription.text.toString()
            val website = editTextWebsite.text.toString()
            val password = editTextCurrentPassword.text.toString()

            if(username.isEmpty()){
                editTextUsername.error = getString(R.string.profile_username_required)
            } else if(email.isEmpty()){
                editTextEmail.error = getString(R.string.profile_email_required)
            } else if(password.isEmpty()){
                editTextCurrentPassword.error = getString(R.string.profile_password_required)
            } else {
                val requestUserProfile: RequestUserProfile = RequestUserProfile(
                        username,
                        email,
                        description,
                        website,
                        password
                )

                profileViewModel.updateProfile(requestUserProfile)
                Toast.makeText(activity, R.string.profile_sending_to_server, Toast.LENGTH_SHORT).show()
                buttonSave.isEnabled = false
            }
        }

        buttonChangePassword.setOnClickListener {
            // TODO: Implement functionality
            Toast.makeText(activity, "Click on change password", Toast.LENGTH_SHORT).show()
        }

        profileViewModel.userProfile.observe(activity, object : Observer<ResponseUserProfile>{
            override fun onChanged(responseUserProfile: ResponseUserProfile?) {
                loadingData = false
                editTextUsername.setText(responseUserProfile?.username)
                editTextEmail.setText(responseUserProfile?.email)
                editTextWebsite.setText(responseUserProfile?.website)
                editTextDescription.setText(responseUserProfile?.description)

                if(responseUserProfile!!.photoUrl.isNotEmpty()){
                    Glide.with(activity!!)
                            .load("${Constants.API_MINITWITTER_FILES_URL}${responseUserProfile.photoUrl}")
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop().skipMemoryCache(true)
                            .into(imageViewAvatar)
                }

                if(!loadingData){
                    buttonSave.isEnabled = true
                    Toast.makeText(activity, "Data saved", Toast.LENGTH_SHORT).show()
                }
            }
        })

        profileViewModel.photoProfile.observe(this, object : Observer<String>{
            override fun onChanged(photo: String?) {
                Glide.with(activity!!)
                        .load("${Constants.API_MINITWITTER_FILES_URL}${photo}")
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(imageViewAvatar)
            }
        })

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel = ViewModelProviders.of(activity).get(ProfileViewModel::class.java)
    }

    private fun checkPermissions(){
        val dialogOnDeniedPermissionListener:PermissionListener=
                DialogOnDeniedPermissionListener.Builder.withContext(activity)
                        .withTitle(R.string.profile_permission_needed_to_select_photo_title)
                        .withMessage(R.string.profile_permission_needed_to_select_photo_body)
                        .withButtonText(R.string.profile_permission_needed_to_select_photo_accept_button)
                        .withIcon(R.mipmap.ic_launcher)
                        .build()

        allPermissionsListener = CompositePermissionListener(
                activity as PermissionListener,
                dialogOnDeniedPermissionListener
        )

        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .check()
    }
}