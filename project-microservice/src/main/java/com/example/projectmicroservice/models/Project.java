package com.example.projectmicroservice.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", updatable=false,nullable=false)
    private UUID id;

    @Column(name = "name", nullable=false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length=255)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt= LocalDateTime.now();

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;
}
