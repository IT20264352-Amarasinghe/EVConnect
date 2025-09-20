package com.evconnect.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evconnect.R;
import com.evconnect.activities.LoginActivity;
import com.evconnect.models.UserInfo;
import com.evconnect.utils.JwtUtils;
import com.evconnect.utils.TokenManager;

public class ProfileFragment extends Fragment {

    private TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tokenManager = new TokenManager(requireContext());

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvNic = view.findViewById(R.id.tvNic);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvRole = view.findViewById(R.id.tvRole);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        String token = tokenManager.getToken();

        if (token != null) {
            UserInfo user = JwtUtils.extractUserInfo(token);

            tvName.setText(user.name);
            tvNic.setText(user.nic);
            tvEmail.setText(user.email);
            tvRole.setText(user.role);
        }

        btnLogout.setOnClickListener(v -> {
            tokenManager.clearToken();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}

