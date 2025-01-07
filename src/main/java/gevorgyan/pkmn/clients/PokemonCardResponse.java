package gevorgyan.pkmn.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PokemonCardResponse {
    @JsonProperty("data")
    private final List<CardData> data;

    @Data
    public static class CardData {
        private String id;
        private String name;
        private String imageUrl;

        @JsonProperty("images")
        private Images images;

        @Data
        public static class Images {
            private String small;
            private String large;
        }
    }
}