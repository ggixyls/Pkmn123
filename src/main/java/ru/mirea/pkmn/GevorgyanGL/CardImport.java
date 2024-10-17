package ru.mirea.pkmn.GevorgyanGL;

import ru.mirea.pkmn.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CardImport {
    private String filename;

    public Card importCard(String filename) {
        Card card = new Card();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            List<String> cardData = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                cardData.add(line);
            }

            PokemonStage pokemonStage = PokemonStage.valueOf(cardData.get(0).toUpperCase());
            card.setPokemonStage(pokemonStage);

            String name = cardData.get(1);
            card.setName(name);

            int hp = Integer.parseInt(cardData.get(2));
            card.setHp(hp);

            EnergyType pokemonType = EnergyType.valueOf(cardData.get(3).toUpperCase());
            card.setPokemonType(pokemonType);

            String parentStr = cardData.get(4);
            if (parentStr.endsWith(".txt")) {
                card.evolvesFrom = importCard(parentStr);
            }
            else if (parentStr.equals("-")) {
                card.evolvesFrom = null;
            }
            else {
                card.evolvesFrom = importCard(parentStr);
            }

            String[] attacks = cardData.get(5).split(",");
            List<AttackSkill> skills = new ArrayList<>();
            for (String attackString : attacks) {
                String[] attackParts = attackString.split("/");
                String attackCoast = attackParts[0];
                String attackName = attackParts[1];
                int attackDamage = Integer.parseInt(attackParts[2]);
                skills.add(new AttackSkill(attackName, attackCoast, attackDamage));
            }
            card.setSkills(skills);

            EnergyType weaknessType = EnergyType.valueOf(cardData.get(6).toUpperCase());
            card.setWeaknessType(weaknessType);

            EnergyType resistanceType = null;
            if (!cardData.get(7).toUpperCase().equals("-")) {
                resistanceType = EnergyType.valueOf(cardData.get(7).toUpperCase());
            }
            card.setResistanceType(resistanceType);

            String retreatCost = cardData.get(8);
            card.setRetreatCost(retreatCost);

            String gameSet = cardData.get(9);
            card.setGameSet(gameSet);

            char regulationMark = cardData.get(10).charAt(0);
            card.setRegulationMark(regulationMark);

            String[] ownerData = cardData.get(11).split("/");
            Student pokemonOwner = new Student(ownerData[0], ownerData[1], ownerData[2], ownerData[3]);
            card.setPokemonOwner(pokemonOwner);

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return card;
    }

    public Card importCardFromFile(String filename) {
        Card card1 = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            card1 = (Card) in.readObject();
            System.out.println("Карта была успешно загружена из файла: " + filename + "\n");
            card1.Info();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке карты: " + e.getMessage());
        }
        return card1;
    }
}