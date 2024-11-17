package ru.mirea.pkmn.GevorgyanGL.web.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

        try {
            Statement st = connection.createStatement();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (type == null) {
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
    public Card getCardFromDatabase(String cardName) {
        Card card = null; // Объявляем переменную card вне блока try
        String cardOwnerId = null; // Переменная для хранения id владельца покемона
        String evolvesFrom = null; // Переменная для хранения id покемона-родителя

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM card WHERE name = '" + cardName + "';");

            if (rs.next()) {
                String name = rs.getString("name");
                int hp = Integer.parseInt(rs.getString("hp"));
                String game_set = rs.getString("game_set");
                String stage = rs.getString("stage");
                String retreat_cost = rs.getString("retreat_cost");
                String weakness_type = rs.getString("weakness_type");
                String resistance_type = rs.getString("resistance_type");
                String attack_skill = rs.getString("attack_skills");
                cardOwnerId = rs.getString("pokemon_owner");
                evolvesFrom = rs.getString("evolves_from");

                String evolvesUuidFromDB = rs.getString("evolves_from");
                ;
                Card evolvesCard;
                if (evolvesUuidFromDB != null) {
                    try {
                        UUID evolvesUUID = UUID.fromString(evolvesUuidFromDB);
                        evolvesCard = getEvolvesFrom(evolvesUUID);
                        card.setEvolvesFrom(evolvesCard);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Считываем навыки атаки
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

                String pokemon_type = rs.getString("pokemon_type");
                char regulation_mark = rs.getString("regulation_mark").charAt(0);
                String card_number = rs.getString("card_number");

                // Создаем объект Card с временными данными
                card = new Card(
                        setStage(stage),
                        name,
                        hp,
                        setType(pokemon_type),
                        null,  // На данный момент эволюция - null
                        attacksList,
                        setType(weakness_type),
                        setType(resistance_type),
                        retreat_cost,
                        game_set,
                        regulation_mark,
                        null, // Временно null для владельца, получим позже
                        card_number
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Возвращаем null в случае ошибки
        }

        // После закрытия Statement, получаем владельца покемона
        if (card != null && cardOwnerId != null) {
            try (Statement ownerStatement = connection.createStatement()) {
                ResultSet ownerResultSet = ownerStatement.executeQuery(
                        "SELECT * FROM student WHERE id = '" + cardOwnerId + "';");
                if (ownerResultSet.next()) {
                    Student pokemon_owner = new Student(
                            ownerResultSet.getString("familyName"),
                            ownerResultSet.getString("firstName"),
                            ownerResultSet.getString("patronicName"),
                            ownerResultSet.getString("group"));
                    card.setPokemonOwner(pokemon_owner); // Устанавливаем владельца покемона
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // После получения владельца, получаем информацию о родительском покемоне
        if (card != null && evolvesFrom != null) {
            Card parentCard = getCardFromDatabase(evolvesFrom);
            card.setEvolvesFrom(parentCard); // Устанавливаем родительский покемон
        }

        return card; // Возвращаем объект Card
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

            // Передаем название покемона для эволюции и фамилию владельца
            if (card.getEvolvesFrom() != null){
                saveCardToDatabase(card.getEvolvesFrom());
                preparedStatement.setString(3, card.getEvolvesFrom().getName());
            }
            else{
                preparedStatement.setString(3,  null);
            }
            preparedStatement.setString(4, card.getGameSet());
            preparedStatement.setString(5, card.getEvolvesFrom() != null ? card.getPokemonOwner().getSurName() : null);
            preparedStatement.setString(6, card.getPokemonStage().name());
            preparedStatement.setString(7, card.getRetreatCost());
            preparedStatement.setString(8, card.getWeaknessType() != null ? card.getWeaknessType().name() : null);
            preparedStatement.setString(9, card.getResistanceType() != null ? card.getResistanceType().name() : null);

            // Сериализуем список атак в JSON
            Gson gson = new GsonBuilder().create();
            String attackSkillsJson = gson.toJson(card.getSkills());
            // Преобразуем строку в JSON
            preparedStatement.setObject(10, attackSkillsJson, java.sql.Types.OTHER);

            preparedStatement.setString(11, card.getPokemonType().name());
            preparedStatement.setString(12, String.valueOf(card.getRegulationMark()));
            preparedStatement.setString(13, card.getNumber());

            preparedStatement.executeUpdate();

            if (card.getEvolvesFrom() != null) {
                saveCardToDatabase(card.getEvolvesFrom());
            }
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


    private Card getEvolvesFrom(UUID evolvesUUID) throws IOException {
        String query = "SELECT * FROM card WHERE id = ?";
        Properties databaseProperties = new Properties();

        try {
            databaseProperties.load(new FileInputStream("src/main/resources/database.properties"));
            try (Connection connection = DriverManager.getConnection(
                    databaseProperties.getProperty("database.url"),
                    databaseProperties.getProperty("database.user"),
                    databaseProperties.getProperty("database.password"));
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, evolvesUUID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Card card = new Card();

                        card.setName(resultSet.getString("name"));
                        card.setHp(resultSet.getInt("hp"));

                        String nextEvolvesUuid = resultSet.getString("evolves_from");
                        if (nextEvolvesUuid != null && !nextEvolvesUuid.isEmpty()) {
                            try {
                                UUID nextEvolvesUUID = UUID.fromString(nextEvolvesUuid);
                                card.setEvolvesFrom(getEvolvesFrom(nextEvolvesUUID));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        card.setGameSet(resultSet.getString("game_set"));
                        card.setPokemonStage(PokemonStage.valueOf(resultSet.getString("stage")));
                        card.setRetreatCost(resultSet.getString("retreat_cost"));
                        card.setWeaknessType(EnergyType.valueOf(resultSet.getString("weakness_type")));

                        String resistanceTypeValue = resultSet.getString("resistance_type");
                        EnergyType resistanceType = null;
                        if (resistanceTypeValue != null && !resistanceTypeValue.isEmpty()) {
                            try {
                                resistanceType = EnergyType.valueOf(resistanceTypeValue);
                            } catch (IllegalArgumentException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        card.setResistanceType(resistanceType);

                        String attackSkillsJson = resultSet.getString("attack_skills");
                        Gson gson = new Gson();
                        List<AttackSkill> attackSkills = gson.fromJson(attackSkillsJson, new TypeToken<List<AttackSkill>>() {
                        }.getType());
                        card.setSkills(attackSkills);

                        card.setPokemonType(EnergyType.valueOf(resultSet.getString("pokemon_type")));
                        card.setRegulationMark((resultSet.getString("regulation_mark")).charAt(0));
                        card.setNumber(resultSet.getString("card_number"));

                        return card;
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

}