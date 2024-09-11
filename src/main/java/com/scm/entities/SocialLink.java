package com.scm.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long socialLinkId;

    private String link;
    private String title;

}
