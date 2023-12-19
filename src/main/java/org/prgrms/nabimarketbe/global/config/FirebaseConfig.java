package org.prgrms.nabimarketbe.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    private FirebaseApp firebaseApp;

    @PostConstruct
    void init() throws IOException {
        InputStream serviceAccount = new ClassPathResource("firebase/nabimarket_firebase_service_key.json")
                .getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://nabi-market.firebaseio.com")
                .build();

        firebaseApp = FirebaseApp.initializeApp(options);
    }

    @Bean
    Firestore firestore() {
        return FirestoreClient.getFirestore(firebaseApp);
    }
}
