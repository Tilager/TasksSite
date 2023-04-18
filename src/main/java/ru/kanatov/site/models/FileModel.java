package ru.kanatov.site.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="files")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DirectoryModel directory;
}
