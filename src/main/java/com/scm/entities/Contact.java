package com.scm.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contactId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    private String imagePublicId;
    @Column(length = 1000)
    private String description;
    private boolean favourite = false;
    private String websiteLink;
    private String linkedinLink;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Override
    public String toString() {
        return "Contact{" +
                "address='" + address + '\'' +
                ", contactId=" + contactId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", picture='" + picture + '\'' +
                ", imagePublicId='" + imagePublicId + '\'' +
                ", description='" + description + '\'' +
                ", favourite=" + favourite +
                ", websiteLink='" + websiteLink + '\'' +
                ", linkedinLink='" + linkedinLink + '\'' +
                '}';
    }
}
