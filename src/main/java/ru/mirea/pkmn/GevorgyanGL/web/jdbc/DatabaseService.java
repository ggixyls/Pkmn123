package ru.mirea.pkmn.GevorgyanGL.web.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.Student;

import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseService {
    Card getCardFromDatabase(String cardName);
    Student getStudentFromDatabase(String studentFullName);
    void saveCardToDatabase(Card card);
    void createPokemonOwner(Student owner);
}
