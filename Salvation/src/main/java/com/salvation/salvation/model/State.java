package com.salvation.salvation.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "states")
@Getter
@Setter
@ToString()
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int hp;
    @Column(nullable = false)
    private int mana;
    @Column(nullable = false)
    private int strength;
    @Column(nullable = false)
    private int dexterity;
    @Column(nullable = false)
    private int intelligence;
    @Column(nullable = false)
    private int wisdom;
    @Column(nullable = false)
    private int charisma;
    @Column(nullable = false)
    private int agility;
    @Column(nullable = false)
    private int luck;
    @Column(nullable = false)
    private int resistance;
    @Column(nullable = false)
    private int movement;
    @Column(nullable = false)
    private int speed;
    @Column(nullable = false)
    private int magicResistance;
    @Column(nullable = false)
    private int magicPower;


    @OneToOne
    @JoinColumn(name = "character_id", referencedColumnName = "id")
    private Character character;

}
