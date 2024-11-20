package ru.mirea.pkmn;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Card implements Serializable {
    public static final long serialVersionUID = 1L;

    PokemonStage pokemonStage;
    String name;
    int hp;
    EnergyType pokemonType;
    public Card evolvesFrom;
    List<AttackSkill> skills;
    EnergyType weaknessType;
    EnergyType resistanceType;
    String retreatCost;
    String gameSet;
    char regulationMark;
    Student pokemonOwner;
    String number;
    UUID ID;


    public Card(PokemonStage pokemonStage, String name, int hp, EnergyType pokemonType, Card evolvesFrom,
                List<AttackSkill> skills, EnergyType weaknessType, EnergyType resistanceType,
                String retreatCost, String gameSet, char regulationMark, Student pokemonOwner, String number) {
        this.pokemonStage = pokemonStage;
        this.name = name;
        this.hp = hp;
        this.pokemonType = pokemonType;
        this.evolvesFrom = evolvesFrom;
        this.skills = skills;
        this.weaknessType = weaknessType;
        this.resistanceType = resistanceType;
        this.retreatCost = retreatCost;
        this.gameSet = gameSet;
        this.regulationMark = regulationMark;
        this.pokemonOwner = pokemonOwner;
        this.number=number;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Card() {
    }

    public PokemonStage getPokemonStage() {
        return pokemonStage;
    }

    public void setPokemonStage(PokemonStage pokemonStage) {
        this.pokemonStage = pokemonStage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public EnergyType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(EnergyType pokemonType) {
        this.pokemonType = pokemonType;
    }

    public void setEvolvesFrom(Card evolvesFrom) {
        this.evolvesFrom = evolvesFrom;
    }

    public List<AttackSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<AttackSkill> skills) {
        this.skills = skills;
    }

    public EnergyType getWeaknessType() {
        return weaknessType;
    }

    public void setWeaknessType(EnergyType weaknessType) {
        this.weaknessType = weaknessType;
    }

    public EnergyType getResistanceType() {
        return resistanceType;
    }

    public void setResistanceType(EnergyType resistanceType) {
        this.resistanceType = resistanceType;
    }

    public String getRetreatCost() {
        return retreatCost;
    }

    public void setRetreatCost(String retreatCost) {
        this.retreatCost = retreatCost;
    }

    public Card getEvolvesFrom() {
        return evolvesFrom;
    }

    public String getGameSet() {
        return gameSet;
    }

    public void setGameSet(String gameSet) {
        this.gameSet = gameSet;
    }

    public char getRegulationMark() {
        return regulationMark;
    }

    public void setRegulationMark(Character regulationMark) {
        this.regulationMark = regulationMark;
    }

    public Student getPokemonOwner() {
        return pokemonOwner;
    }

    public void setPokemonOwner(Student pokemonOwner) {
        this.pokemonOwner = pokemonOwner;
    }

    @Override
    public String toString() {
        return "Card{" + '\n' +
                " pokemonStage=" + pokemonStage + '\n' +
                " name='" + name + '\'' + '\n' +
                " hp=" + hp + '\n' +
                " pokemonType=" + pokemonType + '\n' +
                " evolvesFrom=" + evolvesFrom + '\n' +
                " skills=" + skills + '\n' +
                " weaknessType=" + weaknessType + '\n' +
                " resistanceType=" + resistanceType + '\n' +
                " retreatCost='" + retreatCost + '\'' + '\n' +
                " gameSet='" + gameSet + '\'' + '\n' +
                " regulationMark=" + regulationMark + '\n' +
                " pokemonOwner=" + pokemonOwner + '\n' +
                " number=" + number + '\n' +
                '}';
    }

    public void Info() {
        System.out.println("Стадия: " + pokemonStage);
        System.out.println("Имя: " + name);
        System.out.println("HP: " + hp);
        System.out.println("Тип покемона: " + pokemonType);
        if (evolvesFrom==null){
            System.out.println("Из какого покемона эволюционирует: " + evolvesFrom);
        }
        else {
            System.out.println("Из какого покемона эволюционирует: " + evolvesFrom.getName());
        }
        System.out.println("Способности атак: " + skills);
        System.out.println("Тип слабости: " + weaknessType);
        System.out.println("Тип сопротивления: " + resistanceType);
        System.out.println("Цена побега: " + retreatCost);
        System.out.println("Название сета: " + gameSet);
        System.out.println("Отметка легальности: " + regulationMark);
        if (pokemonOwner != null) {
            System.out.println("Владелец карты: " + pokemonOwner);
        }
        System.out.println("Номер карты: " + number);
        if (evolvesFrom != null) {
            System.out.println("\nИнформация о родителе покемона:");
            evolvesFrom.Info();
        }
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID id) {
        this.ID = id;
    }
}
