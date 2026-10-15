package com.example.rpgnexus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgnexus.MainActivity;
import com.example.rpgnexus.databinding.ActivityLoginBinding;
import com.example.rpgnexus.repository.AppRepository;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AppRepository.getInstance(this);

        // Auto-navigate if already logged in or in guest mode
        if (repository.getCurrentUser() != null || repository.isGuest()) {
            // If user clicked logout previously, repository handles resetting session
        }

        binding.btnLogin.setOnClickListener(v -> performLogin());
        binding.btnGuest.setOnClickListener(v -> enterAsGuest());
        binding.tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void performLogin() {
        String email = binding.etLoginEmail.getText() != null ? binding.etLoginEmail.getText().toString().trim() : "";
        String password = binding.etLoginPassword.getText() != null ? binding.etLoginPassword.getText().toString().trim() : "";

        if (email.isEmpty()) {
            binding.tilLoginEmail.setError("Informe o e-mail");
            return;
        } else {
            binding.tilLoginEmail.setError(null);
        }

        if (password.isEmpty()) {
            binding.tilLoginPassword.setError("Informe a senha");
            return;
        } else {
            binding.tilLoginPassword.setError(null);
        }

        boolean success = repository.login(email, password);
        if (success) {
            Toast.makeText(this, "Bem-vindo, " + repository.getCurrentUser().getDisplayName() + "!", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } else {
            Toast.makeText(this, "E-mail/senha incorretos ou usuário bloqueado.", Toast.LENGTH_LONG).show();
        }
    }

    private void enterAsGuest() {
        repository.loginGuest();
        Toast.makeText(this, "Modo Convidado ativado.", Toast.LENGTH_SHORT).show();
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
