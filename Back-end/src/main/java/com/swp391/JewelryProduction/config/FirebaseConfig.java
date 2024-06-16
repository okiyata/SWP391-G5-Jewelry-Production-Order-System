package com.swp391.JewelryProduction.config;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Configuration
public class FirebaseConfig {
    @Value("${firebase.url}")
    private String CONFIG_PATH;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (!firebaseApps.isEmpty()) {
            for (FirebaseApp app : firebaseApps) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    return app; // Return the existing instance
                }
            }
        }
        ClassPathResource serviceKeyResource = new ClassPathResource("serviceKey.json");
        InputStream serviceAccount = serviceKeyResource.getInputStream();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("chat-d8802.appspot.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
