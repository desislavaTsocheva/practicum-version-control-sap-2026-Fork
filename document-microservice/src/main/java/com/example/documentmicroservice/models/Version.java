package models;
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
@Table(name = "versions")
public class Version {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", updatable=false,nullable=false)
    private UUID id;

    @Column(name = "version_number", nullable =false, length=255)
    private int versionNumber;

    @Lob
    @Column(name = "message", nullable = false,length=255)
    private String message;

    @Column(name = "created_at", nullable =false)
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
