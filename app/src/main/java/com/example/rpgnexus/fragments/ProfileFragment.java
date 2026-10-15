package com.example.rpgnexus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.rpgnexus.activities.LoginActivity;
import com.example.rpgnexus.databinding.FragmentProfileBinding;
import com.example.rpgnexus.models.User;
import com.example.rpgnexus.repository.AppRepository;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AppRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = AppRepository.getInstance(requireContext());

        updateProfileUI();

        binding.btnProfileLogout.setOnClickListener(v -> {
            repository.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        binding.btnResetDatabase.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Restaurar Dados")
                    .setMessage("Deseja redefinir o banco de dados e restaurar as fichas padrão?")
                    .setPositiveButton("Restaurar", (dialog, which) -> {
                        repository.resetDatabase();
                        Toast.makeText(requireContext(), "Banco de dados restaurado!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfileUI();
    }

    private void updateProfileUI() {
        if (repository.isGuest()) {
            binding.tvProfileName.setText("Usuário Convidado");
            binding.tvProfileEmail.setText("Modo de apenas visualização");
            binding.tvProfileRole.setText("TIPO: CONVIDADO");
            binding.btnProfileLogout.setText("Fazer Login / Cadastrar");
        } else {
            User user = repository.getCurrentUser();
            if (user != null) {
                binding.tvProfileName.setText(user.getDisplayName());
                binding.tvProfileEmail.setText(user.getEmail());
                binding.tvProfileRole.setText(user.isAdmin() ? "TIPO: ADMINISTRADOR" : "TIPO: USUÁRIO CADASTRADO");
                binding.btnProfileLogout.setText("Sair da Conta");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
