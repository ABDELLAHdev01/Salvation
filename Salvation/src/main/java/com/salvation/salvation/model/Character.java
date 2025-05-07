package com.salvation.salvation.model;

import com.salvation.salvation.enums.Races;
import com.salvation.salvation.enums.Sex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "characters")
@Getter
@Setter
@ToString()
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Races race;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private int experience = 0;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private int characterNumber;

    private String description;

    @OneToOne
    private State state;

    @OneToOne
    private User user;

}
