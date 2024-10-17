package ru.mirea.pkmn;

import java.io.Serializable;
import java.util.List;

public class Card implements Serializable {
    public static final long serialVersionUID = 1L;

    PokemonStage pokemonStage;
    String name;
    int hp;
    EnergyType pokemonType;
    public Card evolversFrom;
    List<AttackSkill> skills;
    EnergyType weaknessType;
    EnergyType resistanceType;
    String retreatCost;
    String gameSet;
    char regulationMark;
    Student pokemonOwner;

    public Card(PokemonStage pokemonStage, String name, int hp, EnergyType pokemonType, Card evolversFrom,
                List<AttackSkill> skills, EnergyType weaknessType, EnergyType resistanceType,
                String retreatCost, String gameSet, char regulationMark, Student pokemonOwner) {
        this.pokemonStage = pokemonStage;
        this.name = name;
        this.hp = hp;
        this.pokemonType = pokemonType;
        this.evolversFrom = evolversFrom;
        this.skills = skills;
        this.weaknessType = weaknessType;
        this.resistanceType = resistanceType;
        this.retreatCost = retreatCost;
        this.gameSet = gameSet;
        this.regulationMark = regulationMark;
        this.pokemonOwner = pokemonOwner;
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

    public void setEvolversFrom(Card evolversFrom) {
        this.evolversFrom = evolversFrom;
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

    public String getGameSet() {
        return gameSet;
    }

    public void setGameSet(String gameSet) {
        this.gameSet = gameSet;
    }

    public char getRegulationMark() {
        return regulationMark;
    }

    public void setRegulationMark(char regulationMark) {
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
        return "Card{" +
                "pokemonStage=" + pokemonStage +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", pokemonType=" + pokemonType +
                ", evolversFrom='" + evolversFrom + '\'' +
                ", skills=" + skills +
                ", weaknessType=" + weaknessType +
                ", resistanceType=" + resistanceType +
                ", retreatCost='" + retreatCost + '\'' +
                ", gameSet='" + gameSet + '\'' +
                ", regulationMark='" + regulationMark + '\'' +
                ", pokemonOwner=" + pokemonOwner +
                '}';
    }

    public void Info() {
        System.out.println("Стадия: " + pokemonStage);
        System.out.println("Имя: " + name);
        System.out.println("HP: " + hp);
        System.out.println("Тип покемона: " + pokemonType);
        if (evolversFrom==null){
            System.out.println("Из какого покемона эволюционирует: " + evolversFrom);
        }
        else{
            System.out.println("Из какого покемона эволюционирует: Данные родителя указаны ниже");
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
        if (evolversFrom != null) {
            System.out.println("\nИнформация о родителе покемона:");
            evolversFrom.Info();
        }
    }
}