package com.example.documentmicroservice.models;

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
    private byte[] file_path;


    @Column(name = "content", nullable = false,length = 255)
    private String content;

    @Column(name = "comment", length=255)
    private String comment;

    @Lob
    @Column(name = "draft", columnDefinition = "VARBINARY(MAX)")
    private byte[] draft;

    @Column(name = "history_timestamp")
    private LocalDateTime historyTimestamp;

    @Column(name = "version_id", nullable = false)
    private UUID versionId;

}
