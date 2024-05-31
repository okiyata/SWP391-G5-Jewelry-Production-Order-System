package com.swp391.JewelryProduction.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

import static com.google.api.ResourceProto.resource;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        String firebaseConfigPath = System.getenv("FIREBASE_CONFIG_PATH");

        if (firebaseConfigPath == null) {
            throw new IllegalStateException("FIREBASE_CONFIG_PATH environment variable is not set.");
        }

        FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
