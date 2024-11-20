package ru.mirea.pkmn.GevorgyanGL.web.jdbc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.mirea.pkmn.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DatabaseServiceImpl implements DatabaseService {

    private final Connection connection;
    private final Properties databaseProperties;

    public DatabaseServiceImpl() throws SQLException, IOException {
        databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream("src/main/resources/database.properties"));

        connection = DriverManager.getConnection(
                databaseProperties.getProperty("database.url"),
                databaseProperties.getProperty("database.user"),
                databaseProperties.getProperty("database.password")
        );
        System.out.println("Connection is " + (connection.isValid(0) ? "up" : "down"));
    }

    private PokemonStage setStage(String stage) {
        return switch (stage) {
            case "BASIC" -> PokemonStage.BASIC;
            case "STAGE1" -> PokemonStage.STAGE1;
            case "STAGE2" -> PokemonStage.STAGE2;
            case "VSTAR" -> PokemonStage.VSTAR;
            case "VMAX" -> PokemonStage.VMAX;
            default -> null;
        };
    }

    private EnergyType setType(String type) {
        if (type == null || type.equalsIgnoreCase("null")) {
            return null;
        }
        return switch (type) {
            case "FIRE" -> EnergyType.FIRE;
            case "GRASS" -> EnergyType.GRASS;
            case "WATER" -> EnergyType.WATER;
            case "LIGHTNING" -> EnergyType.LIGHTNING;
            case "PSYCHIC" -> EnergyType.PSYCHIC;
            case "FIGHTING" -> EnergyType.FIGHTING;
            case "DARKNESS" -> EnergyType.DARKNESS;
            case "METAL" -> EnergyType.METAL;
            case "FAIRY" -> EnergyType.FAIRY;
            case "DRAGON" -> EnergyType.DRAGON;
            case "COLORLESS" -> EnergyType.COLORLESS;
            default -> null;
        };
    }

    @Override
    public Card getCardFromDatabase(String cardName) throws IOException {
        Card card = null;
        String evolvesFrom = null;

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM card WHERE name = '" + cardName + "' AND evolves_from IS NOT NULL;");

            if (rs.next()) {
                String name = rs.getString("name");
                int hp = Integer.parseInt(rs.getString("hp"));
                String game_set = rs.getString("game_set");
                String stage = rs.getString("stage");
                String retreat_cost = rs.getString("retreat_cost");
                String weakness_type = rs.getString("weakness_type");
                String resistance_type = rs.getString("resistance_type");
                String attack_skill = rs.getString("attack_skills");
                String  ownerUuidFromDB = rs.getString("pokemon_owner");


                evolvesFrom = rs.getString("evolves_from");

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(attack_skill);
                List<AttackSkill> attacksList = new ArrayList<>();
                for (JsonNode attackNode : jsonNode) {
                    AttackSkill attack = new AttackSkill(
                            attackNode.path("name").asText(),
                            attackNode.path("description").asText(),
                            attackNode.path("cost").asText(),
                            attackNode.path("damage").asInt()
                    );
                    attacksList.add(attack);
                }
                Student owner = ownerUuidFromDB != null ? getPokemonOwner(UUID.fromString(ownerUuidFromDB)) : null;


                String pokemon_type = rs.getString("pokemon_type");
                char regulation_mark = rs.getString("regulation_mark").charAt(0);
                String card_number = rs.getString("card_number");

                card = new Card(
                        setStage(stage),
                        name,
                        hp,
                        setType(pokemon_type),
                        null,
                        attacksList,
                        setType(weakness_type),
                        setType(resistance_type),
                        retreat_cost,
                        game_set,
                        regulation_mark,
                        owner,
                        card_number
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (card != null && evolvesFrom != null) {
            Card parentCard = getCardFromDatabaseById(UUID.fromString(evolvesFrom));
            if (parentCard != null) {
                card.setEvolvesFrom(parentCard);
            }
        }
        return card;
    }

    private Card getCardFromDatabaseById(UUID evolvesFromId) throws IOException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM card WHERE id = ?")) {
            ps.setObject(1, evolvesFromId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                int hp = rs.getInt("hp");
                String game_set = rs.getString("game_set");
                String stage = rs.getString("stage");
                String retreat_cost = rs.getString("retreat_cost");
                String weakness_type = rs.getString("weakness_type");
                String resistance_type = rs.getString("resistance_type");
                String attack_skill = rs.getString("attack_skills");
                String pokemon_type = rs.getString("pokemon_type");
                char regulation_mark = rs.getString("regulation_mark").charAt(0);
                String card_number = rs.getString("card_number");

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(attack_skill);
                List<AttackSkill> attacksList = new ArrayList<>();
                for (JsonNode attackNode : jsonNode) {
                    AttackSkill attack = new AttackSkill(
                            attackNode.path("name").asText(),
                            attackNode.path("description").asText(),
                            attackNode.path("cost").asText(),
                            attackNode.path("damage").asInt()
                    );
                    attacksList.add(attack);
                }

                return new Card(
                        setStage(stage),
                        name,
                        hp,
                        setType(pokemon_type),
                        null,
                        attacksList,
                        setType(weakness_type),
                        setType(resistance_type),
                        retreat_cost,
                        game_set,
                        regulation_mark,
                        null,
                        card_number
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Student getStudentFromDatabase(String studentFullName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM student WHERE \"familyName\" = '" + studentFullName + "';");

            Student student = null;
            while (resultSet.next()) {
                String familyName = resultSet.getString("familyName");
                String firstName = resultSet.getString("firstName");
                String patronicName = resultSet.getString("patronicName");
                String group = resultSet.getString("group");

                student = new Student(familyName, firstName, patronicName, group);
                break;
            }

            statement.close();
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveCardToDatabase(Card card) {
        String sql = "INSERT INTO card (id, name, hp, evolves_from, game_set, pokemon_owner, stage, retreat_cost, weakness_type, resistance_type, attack_skills, pokemon_type, regulation_mark, card_number) " +
                "VALUES (gen_random_uuid(), ?, ?, (SELECT id FROM card WHERE name = ? LIMIT 1), ?, (SELECT id FROM student WHERE \"familyName\" = ? LIMIT 1), ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, card.getName());
            preparedStatement.setInt(2, card.getHp());

            if (card.getEvolvesFrom() != null) {
                saveCardToDatabase(card.getEvolvesFrom());
                preparedStatement.setString(3, card.getEvolvesFrom().getName());
            } else {
                preparedStatement.setString(3, null);
            }

            preparedStatement.setString(4, card.getGameSet());
            preparedStatement.setString(5, card.getPokemonOwner() != null ? card.getPokemonOwner().getSurName() : null);
            preparedStatement.setString(6, card.getPokemonStage().name());
            preparedStatement.setString(7, card.getRetreatCost());
            preparedStatement.setString(8, card.getWeaknessType() != null ? card.getWeaknessType().name() : null);
            preparedStatement.setString(9, card.getResistanceType() != null ? card.getResistanceType().name() : null);

            Gson gson = new GsonBuilder().create();
            String attackSkillsJson = gson.toJson(card.getSkills());
            preparedStatement.setObject(10, attackSkillsJson, java.sql.Types.OTHER);

            preparedStatement.setString(11, card.getPokemonType().name());
            preparedStatement.setString(12, String.valueOf(card.getRegulationMark()));
            preparedStatement.setString(13, card.getNumber());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPokemonOwner(Student owner) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(STR."INSERT INTO student VALUES(gen_random_uuid(), '\{owner.getSurName()}', '\{owner.getFirstName()}', '\{owner.getFamilyName()}', '\{owner.getGroup()}');");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(STR."Success \{getStudentFromDatabase(owner.getSurName())}");
    }

    private Student getPokemonOwner(UUID ownerUUID){
        String query = "SELECT * FROM student WHERE id = ?";
        Properties databaseProperties = new Properties();

        try{
            databaseProperties.load(new FileInputStream("src/main/resources/database.properties"));
            try (Connection connection = DriverManager.getConnection(
                    databaseProperties.getProperty("database.url"),
                    databaseProperties.getProperty("database.user"),
                    databaseProperties.getProperty("database.password"));
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, ownerUUID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Student student = new Student();

                        student.setFamilyName(resultSet.getString("patronicName"));
                        student.setFirstName(resultSet.getString("firstName"));
                        student.setSurName(resultSet.getString("familyName"));
                        student.setGroup(resultSet.getString("group"));

                        return student;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Ошибка получения карты из базы данных: " + e.getMessage());
                return null;
            }
            return null;
        } catch (IOException e) {
            System.err.println("Ошибка загрузки файла: " + e.getMessage());
        }
        return null;
    }


    public void getAllStudents() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM student;");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        while (resultSet.next()) {
            for (int i = 1; i <= 5; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }

    public void getAllCards() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM card;");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        while (resultSet.next()) {
            for (int i = 1; i <= 14; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }
}