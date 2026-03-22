package com.purohitdarpan.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.service-account-path}")
    private String serviceAccountPath;

    @Value("${firebase.project-id}")
    private String projectId;

    @PostConstruct
    public void initializeFirebase() {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                InputStream serviceAccount = loadServiceAccount();
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setProjectId(projectId)
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized for project: {}", projectId);
            } catch (IOException e) {
                log.warn("Firebase initialization failed (notifications may not work): {}", e.getMessage());
                // App continues without Firebase in dev/test
            }
        }
    }

    private InputStream loadServiceAccount() throws IOException {
        // Try classpath first, then filesystem
        InputStream is = getClass().getClassLoader().getResourceAsStream(serviceAccountPath);
        if (is != null) return is;

        return new FileInputStream(serviceAccountPath);
    }
}
