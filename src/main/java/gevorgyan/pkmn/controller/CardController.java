package gevorgyan.pkmn.controller;

import gevorgyan.pkmn.clients.PokemonTcgService;
import gevorgyan.pkmn.entity.CardEntity;
import gevorgyan.pkmn.entity.StudentEntity;
import gevorgyan.pkmn.models.Card;
import gevorgyan.pkmn.models.Student;
import gevorgyan.pkmn.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {
    @Autowired
    private final CardService cardService;
    private final PokemonTcgService tcg;


    @GetMapping("")
    public List<CardEntity> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CardEntity> getCardById(@PathVariable UUID id) {
        CardEntity card = cardService.getCardById(id);
        return card != null ? ResponseEntity.ok(card) : ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<CardEntity> createCard(@RequestBody CardEntity card) {
        CardEntity savedCard = cardService.saveCard(card);
        return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }

    @PutMapping("/id/{id}")
    public CardEntity updateCard(@PathVariable UUID id, @RequestBody CardEntity card) {
        return cardService.updateCard(id, card);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owner")
            public List<CardEntity> getCardsByOwner(@RequestBody StudentEntity ownerRequest) {
            return cardService.getCardsByOwner(ownerRequest.getFirstName(), ownerRequest.getSurName(), ownerRequest.getFamilyName());
        }

    @GetMapping("/{name}")
    public List<CardEntity> getCardsByName(@PathVariable String name) {
        return cardService.getCardsByName(name);
    }

    @GetMapping("/image")
    public ResponseEntity<Void> getCardImage(@RequestBody Card card) {
        try {
            String imageUrl = tcg.getCardImageUrl(card.getName(), card.getNumber());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(imageUrl));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}