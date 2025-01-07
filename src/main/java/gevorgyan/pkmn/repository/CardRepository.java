package gevorgyan.pkmn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import gevorgyan.pkmn.entity.CardEntity;

import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    @Query(
            """
                    SELECT card
                    FROM CardEntity as card
                    WHERE card.pokemonOwner.firstName = :firstName
                      AND card.pokemonOwner.surName = :surName
                      AND card.pokemonOwner.familyName = :familyName
                    """
    )
    List<CardEntity> findByPokemonOwner(String firstName, String surName, String familyName);
    List<CardEntity> findByName(String name);
}