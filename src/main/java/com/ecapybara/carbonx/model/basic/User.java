package com.ecapybara.carbonx.model.basic;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data @Builder
@Document("users")
@PersistentIndex(fields = {"email", "username"})
public class User {

    @Id // db document field: _key
    private String id;

    @ArangoId // db document field: _id
    private String arangoId;

    @NonNull
    private String username;

    @NonNull
    private String email;
    
    @NonNull
    private String password; // Note: In production, this should be hashed

    @NonNull
    private String firstName;

    private String lastName;
    
    @NonNull @Builder.Default
    private String role = "user"; // e.g., "admin", "user", "supplier"

    @NonNull
    private String companyName;

    private Boolean active;

    @NonNull
    private final LocalDateTime createdAt;
    
    @NonNull
    private final LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + 
               ", firstName=" + firstName + ", lastName=" + lastName + 
               ", role=" + role + ", active=" + active + "]";
    }
}

