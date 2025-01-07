package gevorgyan.pkmn.service;

import gevorgyan.pkmn.entity.CardEntity;

import java.util.List;
import java.util.UUID;

public interface CardService {
    List<CardEntity> getAllCards();
    CardEntity getCardById(UUID id);
    CardEntity saveCard(CardEntity card);
    CardEntity updateCard(UUID id, CardEntity card);
    void deleteCard(UUID id);
    List<CardEntity> getCardsByOwner(String firstName, String surName, String familyName);
    List<CardEntity> getCardsByName(String name);
    String getCardImageByName(String cardName); // Новый метод
}