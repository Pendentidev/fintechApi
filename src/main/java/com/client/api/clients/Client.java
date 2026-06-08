package com.client.api.clients;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    private String name;
    private String surnameOrLegalName;
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    private String documentNumber;
    private String address;
    private String phoneNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    private LocalDate registrationDate;
    private Boolean active;

}
