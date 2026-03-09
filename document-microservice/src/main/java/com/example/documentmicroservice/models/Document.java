package models;
import com.example.projectmicroservice.models.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", updatable=false,nullable=false)
    private UUID id;

    @Column(name = "name", nullable =false, length = 50)
    private String name;

    @Column(name = "description", nullable =false, length = 500)
    private String description;

    @Column(name = "created_at", nullable =false)
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

}
