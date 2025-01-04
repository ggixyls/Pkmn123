package gevorgyan.pkmn;

import java.io.Serializable;

public class AttackSkill implements Serializable {
    public static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String cost;
    private int damage;

    public AttackSkill(String name, String descripion, String cost, int damage) {
        this.name = name;
        this.description = descripion;
        this.cost = cost;
        this.damage = damage;
    }

    public String getDescription() {return description;}

    public void setDescription(String descripion) {this.description = descripion;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "AttackSkill{" +
                "name='" + name + '\'' +
                ", descripion='" + description + '\'' +
                ", cost='" + cost + '\'' +
                ", damage=" + damage +
                '}';
    }
}
