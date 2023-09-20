package com.chatapp.chatApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String dob;
    private String gender;
    private String password;
    private String email;
    private String city;
    private String state;
    private String country;
    private String pincode;

}
