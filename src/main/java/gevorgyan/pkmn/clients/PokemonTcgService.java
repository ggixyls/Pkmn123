package gevorgyan.pkmn.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gevorgyan.pkmn.service.RestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor

public class PokemonTcgService {

    private final RestClient restClient;
    private static final String API_URL_name = "https://api.pokemontcg.io/v2/cards?q=name:";
    private static final String API_URL_number = " AND number:";

    public String getCardImageUrl(String cardName, String cardNumber) {
        String url = API_URL_name + cardName + API_URL_number + cardNumber;
        System.out.println("Trying to get json... via url: " + url);
        String jsonResponse = restClient.get(url);
        System.out.println("Got it");
        JsonNode dataNode = parseJson(jsonResponse).path("data");

        if (!dataNode.isArray() || dataNode.isEmpty()) {
            throw new RuntimeException("Card not found: " + cardName);
        }

        JsonNode cardNode = dataNode.get(0);
        JsonNode imagesNode = cardNode.path("images");

        if (!imagesNode.has("small")) {
            throw new RuntimeException("Image not available for card: " + cardName);
        }

        return imagesNode.get("small").asText();
    }

    private JsonNode parseJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
}