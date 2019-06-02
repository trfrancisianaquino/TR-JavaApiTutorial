package com.example.javaApiTutorial.entities;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.Email;

@Embeddable
@Data
public class ContactInfo {

    @Email(message = "invalid email")
    private String email;
    private String mobile, telephone;
}