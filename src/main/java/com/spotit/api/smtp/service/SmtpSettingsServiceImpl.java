package com.spotit.api.smtp.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.smtp.entity.SmtpSettings;
import com.spotit.api.smtp.repository.SmtpSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmtpSettingsServiceImpl implements SmtpSettingsService {

    private final SmtpSettingsRepository repository;
    private final EncryptionService encryptionService;

    @Override
    @Transactional(readOnly = true)
    public Optional<ResolvedSmtpSettings> getActiveSettings() {
        return repository.findTopByOrderByUpdatedAtDesc()
                .map(settings -> new ResolvedSmtpSettings(settings.getHost(), settings.getPort(), settings.getUsername(),
                        encryptionService.decrypt(settings.getEncryptedPassword()), settings.getFromAddress(), settings.isUseTls()));
    }

    @Override
    @Transactional
    public void saveSettings(String host, int port, String username, String password, String fromAddress, boolean useTls) {
        SmtpSettings settings = repository.findTopByOrderByUpdatedAtDesc().orElseGet(SmtpSettings::new);

        settings.setHost(host);
        settings.setPort(port);
        settings.setUsername(username);
        settings.setFromAddress(fromAddress);
        settings.setUseTls(useTls);
        if (password != null && !password.isBlank()) {
            settings.setEncryptedPassword(encryptionService.encrypt(password));
        } else if (settings.getEncryptedPassword() == null) {
            throw new IllegalArgumentException("password is required when creating SMTP settings for the first time");
        }

        repository.save(settings);
    }
}
