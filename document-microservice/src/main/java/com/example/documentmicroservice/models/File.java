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
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id", updatable=false,nullable=false)
    private UUID id;

    @Lob
    @Column(name = "filepath", nullable =false, length=255)
    private String file_path;

    @Lob
    @Column(name = "content", nullable = false,length = 255)
    private String content;

    @Column(name = "comment" ,nullable=false, length=255)
    private String comment;

    @Column(name = "draft", nullable=false, length=255)
    private String draft;

    @Column(name = "history_timestamp", nullable=false)
    private LocalDateTime historyTimestamp;

    @Column(name = "version_id", nullable = false)
    private UUID versionId;
}
