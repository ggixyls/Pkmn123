package ru.mirea.pkmn.GevorgyanGL.web.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseService {
    Card getCardFromDatabase(String cardName) throws SQLException, IOException;
    Student getStudentFromDatabase(String studentFullName) throws SQLException;
    void saveCardToDatabase(Card card) throws SQLException;
    void createPokemonOwner(Student owner) throws SQLException;
}
