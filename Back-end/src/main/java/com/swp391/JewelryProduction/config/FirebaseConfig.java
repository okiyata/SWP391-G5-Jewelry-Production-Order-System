package com.swp391.JewelryProduction.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Configuration
public class FirebaseConfig {
    @Value("${firebase.url}")
    private String configPath;

    @Bean
    public FirebaseApp initializeFirebase(ResourceLoader resourceLoader) throws IOException {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (!firebaseApps.isEmpty()) {
            for (FirebaseApp app : firebaseApps) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    return app; // Return the existing instance
                }
            }
        }

        Resource resource;
        InputStream serviceAccount;

        if (configPath.startsWith("classpath:")) {
            resource = resourceLoader.getResource(configPath);
            serviceAccount = resource.getInputStream();
        } else {
            // Handle file system path
            resource = resourceLoader.getResource("file:" + configPath);
            serviceAccount = resource.getInputStream();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("chat-d8802.appspot.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
