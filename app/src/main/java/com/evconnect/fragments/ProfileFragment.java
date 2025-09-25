package com.evconnect.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evconnect.R;
import com.evconnect.activities.LoginActivity;
import com.evconnect.db.UserDao;
import com.evconnect.models.UserInfo;
import com.evconnect.utils.JwtUtils;
import com.evconnect.utils.TokenManager;

public class ProfileFragment extends Fragment {

    // A manager to handle JWT tokens, including storage and retrieval.
    private TokenManager tokenManager;

    // A custom object to hold user information extracted from the JWT.
    UserInfo user;

//    Called to have the fragment instantiate its user interface view.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize the TokenManager with the fragment's context.
        tokenManager = new TokenManager(requireContext());

        // Find the TextViews and Button in the layout by their IDs.
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvNic = view.findViewById(R.id.tvNic);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvRole = view.findViewById(R.id.tvRole);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        // Retrieve the token from the TokenManager.
        String token = tokenManager.getToken();

        // Check if the token exists
        if (token != null) {
            user = JwtUtils.extractUserInfo(token);
            // Populate the TextViews with the user's information.
            tvName.setText(user.name);
            tvNic.setText(user.nic);
            tvEmail.setText(user.email);
            tvRole.setText(user.role);

        }else{
            // Handle the case where the user is in offline mode or the token has expired/is missing.
            String currentNic = tokenManager.getCurrentUserNic();
            if (currentNic != null) {
                UserDao userDao = new UserDao(requireContext());
                // Try to get user data from the local database.
                user = userDao.getUserByNic(currentNic);
                if(user != null){
                    // Populate TextViews with locally stored user data.
                    tvName.setText(user.name);
                    tvNic.setText(user.nic);
                    tvEmail.setText(user.email);
                    tvRole.setText(user.role);
                }else{
                    Toast.makeText(getContext(), "User not found on offline. Please login using internet.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Please login with internet. User name not found", Toast.LENGTH_SHORT).show();
            }
        }

        // Set up a click listener for the logout button.
        btnLogout.setOnClickListener(v -> {
            tokenManager.clearToken();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}

