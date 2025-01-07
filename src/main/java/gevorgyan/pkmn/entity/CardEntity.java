package gevorgyan.pkmn.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import gevorgyan.pkmn.models.*;
import jakarta.persistence.*;

import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Table(name = "cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="name")
    private String name;
    @Column(columnDefinition = "smallint")
    private short hp;

    @Column(name="cardNumber")
    private String cardNumber;
    @Enumerated(EnumType.STRING)
    @Column(name="stage")
    private PokemonStage pokemonStage;
    @Column(name="retreat_cost")
    private String retreatCost;

    @Convert(converter = EnergyTypeConverter.class)
    @Column(name="pokemon_type", nullable = true)
    private EnergyType pokemonType;
    @Convert(converter = EnergyTypeConverter.class)
    @Column(name="weakness_type", nullable = true)
    private EnergyType weaknessType;
    @Convert(converter = EnergyTypeConverter.class)
    @Column(name="resistance_type", nullable = true)
    private EnergyType resistanceType;

    @Column(name="game_set")
    private String gameSet;
    @Column(name="regulation_mark")
    private char regulationMark;

    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "pokemon_owner_id")
    private StudentEntity pokemonOwner;

    @JdbcTypeCode(JSON)
    @Column(name="attack_skills", columnDefinition = "JSON")
    private List<AttackSkill> skills;

    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "evolves_from_id")
    private CardEntity evolvesFrom;

    @Override
    public String toString() {
        return "Card{" +
                "pokemonStage=" + pokemonStage +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", evolvesFrom=" + evolvesFrom +
                ", skills=" + skills +
                ", pokemonType=" + pokemonType +
                ", weaknessType=" + weaknessType +
                ", resistanceType=" + resistanceType +
                ", retreatCost='" + retreatCost + '\'' +
                ", gameSet='" + gameSet + '\'' +
                ", regulationMark=" + regulationMark +
                ", owner=" + ((pokemonOwner != null) ? pokemonOwner.toString() : pokemonOwner)+
                '}';
    }
}