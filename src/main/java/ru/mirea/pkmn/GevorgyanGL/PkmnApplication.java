package ru.mirea.pkmn.GevorgyanGL;

import ru.mirea.pkmn.*;

public class PkmnApplication {
    public static void main(String[] args) {

        String filename1 = "src/main/resources/my_card.txt";
        String filename2 = "Glastrier (1).crd";

        CardImport cardImport = new CardImport();
        Card card1 = cardImport.importCard(filename1);
        card1.Info();
        CardExport export = new CardExport();
        export.exportCard(card1);

        cardImport.importCardFromFile(filename2);
    }
}