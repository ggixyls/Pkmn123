package gevorgyan.pkmn.GevorgyanGL;

import gevorgyan.pkmn.Card;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CardExport {

    public void exportCard(Card card) {
        String filename = card.getName() + ".crd";

        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(card);
            System.out.println("\nКарта была сохранена в файл: " + filename);

        } catch (IOException e) {
            System.err.println("Ошибка при сохранении карты: " + e.getMessage());
        }
    }
}
