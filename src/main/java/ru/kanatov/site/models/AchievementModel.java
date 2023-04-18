package ru.kanatov.site.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="achievements")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AchievementModel {
    public static final int TYPE_ACHIEVEMENT = 0;
    public static final int TYPE_COURSE = 1;
    public static final int TYPE_SCIENCE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String uuid;

    @Column
    private int type;
}
