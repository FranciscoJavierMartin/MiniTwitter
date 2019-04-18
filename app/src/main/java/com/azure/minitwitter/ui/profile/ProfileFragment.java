package com.azure.minitwitter.ui.profile;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.azure.minitwitter.R;
import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.data.ProfileViewModel;
import com.azure.minitwitter.retrofit.request.RequestUserProfile;
import com.azure.minitwitter.retrofit.response.ResponseUserProfile;
import com.azure.minitwitter.ui.DashboardActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    ImageView ivAvatar;
    EditText etUsername, etEmail, etPassword, etDescription, etWebsite;
    Button buttonSave, buttonChangePassword;
    boolean loadingData = true;
    PermissionListener allPermissionsListener;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        ivAvatar = v.findViewById(R.id.imageViewAvatar);
        etUsername = v.findViewById(R.id.editTextUsername);
        etEmail = v.findViewById(R.id.editTextEmail);
        etPassword = v.findViewById(R.id.editTextCurrentPassword);
        etDescription = v.findViewById(R.id.editTextDescription);
        etWebsite = v.findViewById(R.id.editTextWebsite);
        buttonChangePassword = v.findViewById(R.id.buttonChangePassword);
        buttonSave = v.findViewById(R.id.buttonSave);

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String description = etDescription.getText().toString();
                String website = etWebsite.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty()) {
                    etUsername.setError(getString(R.string.profile_username_required));
                } else if (email.isEmpty()) {
                    etEmail.setError(getString(R.string.profile_email_required));
                } else if (password.isEmpty()) {
                    etPassword.setError(getString(R.string.profile_password_required));
                } else {
                    RequestUserProfile requestUserProfile = new RequestUserProfile(
                            username,
                            email,
                            description,
                            website,
                            password
                    );
                    profileViewModel.updateProfile(requestUserProfile);
                    Toast.makeText(getActivity(), R.string.profile_sending_to_server, Toast.LENGTH_SHORT).show();
                    buttonSave.setEnabled(false);
                }

            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Click on change password", Toast.LENGTH_SHORT).show();
            }
        });

        // ViewModel
        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(@Nullable ResponseUserProfile responseUserProfile) {
                loadingData = false;
                etUsername.setText(responseUserProfile.getUsername());
                etEmail.setText(responseUserProfile.getEmail());
                etWebsite.setText(responseUserProfile.getWebsite());
                etDescription.setText(responseUserProfile.getDescription());

                if (!responseUserProfile.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constants.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }

                if(!loadingData){
                    buttonSave.setEnabled(true);
                    Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String photo) {
                    Glide.with(getActivity())
                            .load(Constants.API_MINITWITTER_FILES_URL+photo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }

        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
    }

    private void checkPermissions(){
        PermissionListener dialogOnDeniedPermissionListener =
                DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle(R.string.profile_permission_needed_to_select_photo_title)
                .withMessage(R.string.profile_permission_needed_to_select_photo_body)
                .withButtonText(R.string.profile_permission_needed_to_select_photo_accept_button)
                .withIcon(R.mipmap.ic_launcher)
                .build();

        allPermissionsListener = new CompositePermissionListener(
                (PermissionListener) getActivity(),
                dialogOnDeniedPermissionListener
        );

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .check();
    }

}
