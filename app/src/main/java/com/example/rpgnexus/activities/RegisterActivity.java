package com.example.rpgnexus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgnexus.MainActivity;
import com.example.rpgnexus.databinding.ActivityRegisterBinding;
import com.example.rpgnexus.repository.AppRepository;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = AppRepository.getInstance(this);

        binding.btnRegister.setOnClickListener(v -> performRegister());
        binding.tvGoToLogin.setOnClickListener(v -> finish());
    }

    private void performRegister() {
        String name = binding.etRegisterName.getText() != null ? binding.etRegisterName.getText().toString().trim() : "";
        String email = binding.etRegisterEmail.getText() != null ? binding.etRegisterEmail.getText().toString().trim() : "";
        String password = binding.etRegisterPassword.getText() != null ? binding.etRegisterPassword.getText().toString().trim() : "";

        if (name.isEmpty()) {
            binding.tilRegisterName.setError("Informe seu nome");
            return;
        } else {
            binding.tilRegisterName.setError(null);
        }

        if (email.isEmpty()) {
            binding.tilRegisterEmail.setError("Informe o e-mail");
            return;
        } else {
            binding.tilRegisterEmail.setError(null);
        }

        if (password.length() < 4) {
            binding.tilRegisterPassword.setError("A senha deve ter no mínimo 4 caracteres");
            return;
        } else {
            binding.tilRegisterPassword.setError(null);
        }

        boolean success = repository.register(email, password, name);
        if (success) {
            Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar. E-mail já pode estar em uso.", Toast.LENGTH_LONG).show();
        }
    }
}
