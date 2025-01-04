package gevorgyan.pkmn.GevorgyanGL;

import com.fasterxml.jackson.databind.JsonNode;
import gevorgyan.pkmn.AttackSkill;
import gevorgyan.pkmn.Card;
import ru.mirea.pkmn.*;
import gevorgyan.pkmn.GevorgyanGL.web.http.PkmnHttpClient;
import gevorgyan.pkmn.GevorgyanGL.web.jdbc.DatabaseServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, SQLException {
        String filename1 = "src/main/resources/my_card.txt";

        CardImport cardImport = new CardImport();
        Card card1 = cardImport.importCard(filename1);
        card1.Info();


        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();

        JsonNode card = pkmnHttpClient.getPokemonCard(card1.getName(), card1.getNumber());
        System.out.println(card.toPrettyString());

        System.out.println(card.findValues("attacks")
                .stream()
                .map(JsonNode::toPrettyString)
                .collect(Collectors.toSet()));

        JsonNode attacksArray = card.path("data").get(0).path("attacks");
        for (int i = 0; i < attacksArray.size(); i++) {
            JsonNode attackNode = attacksArray.get(i);
            String attackName = attackNode.path("name").asText();

            AttackSkill attackSkill = card1.getSkills().stream()
                    .filter(skill -> skill.getName().equals(attackName))
                    .findFirst()
                    .orElse(null);


            attackSkill.setDescription(attackNode.path("text").asText());

        }

        CardExport cardExport = new CardExport();
        cardExport.exportCard(card1);
        Card card2 = cardImport.importCardFromFile("Palossand.crd");
        DatabaseServiceImpl db = new DatabaseServiceImpl();
        //db.createPokemonOwner(card2.getPokemonOwner());
        //db.saveCardToDatabase(card2);
        System.out.println('\n');
        db.getAllStudents();
        db.getAllCards();
        System.out.println("\nСтудент из БД\n" + db.getStudentFromDatabase("Геворгян"));
        System.out.println("\nКарта из БД\n" + db.getCardFromDatabase("Palossand"));
    }
}