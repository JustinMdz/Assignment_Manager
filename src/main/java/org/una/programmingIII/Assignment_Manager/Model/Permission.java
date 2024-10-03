package org.una.programmingIII.Assignment_Manager.Model;

import jakarta.persistence.*;

@Entity

public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PermissionType name;
}
