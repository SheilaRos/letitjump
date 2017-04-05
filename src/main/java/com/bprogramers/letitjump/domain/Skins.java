package com.bprogramers.letitjump.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Skins.
 */
@Entity
@Table(name = "skins")
public class Skins implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price_game")
    private Long priceGame;

    @Column(name = "price_premium")
    private Long pricePremium;

    @Column(name = "splash_art")
    private String splashArt;

    @NotNull
    @Column(name = "attr", nullable = false)
    private String attr;

    @NotNull
    @Column(name = "attr_value", nullable = false)
    private Integer attrValue;

    @ManyToMany
    @JoinTable(name = "skins_user",
               joinColumns = @JoinColumn(name="skins_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="ID"))
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Skins name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPriceGame() {
        return priceGame;
    }

    public Skins priceGame(Long priceGame) {
        this.priceGame = priceGame;
        return this;
    }

    public void setPriceGame(Long priceGame) {
        this.priceGame = priceGame;
    }

    public Long getPricePremium() {
        return pricePremium;
    }

    public Skins pricePremium(Long pricePremium) {
        this.pricePremium = pricePremium;
        return this;
    }

    public void setPricePremium(Long pricePremium) {
        this.pricePremium = pricePremium;
    }

    public String getSplashArt() {
        return splashArt;
    }

    public Skins splashArt(String splashArt) {
        this.splashArt = splashArt;
        return this;
    }

    public void setSplashArt(String splashArt) {
        this.splashArt = splashArt;
    }

    public String getAttr() {
        return attr;
    }

    public Skins attr(String attr) {
        this.attr = attr;
        return this;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public Integer getAttrValue() {
        return attrValue;
    }

    public Skins attrValue(Integer attrValue) {
        this.attrValue = attrValue;
        return this;
    }

    public void setAttrValue(Integer attrValue) {
        this.attrValue = attrValue;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Skins users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Skins addUser(User user) {
        users.add(user);
        return this;
    }

    public Skins removeUser(User user) {
        users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Skins skins = (Skins) o;
        if (skins.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, skins.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Skins{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", priceGame='" + priceGame + "'" +
            ", pricePremium='" + pricePremium + "'" +
            ", splashArt='" + splashArt + "'" +
            ", attr='" + attr + "'" +
            ", attrValue='" + attrValue + "'" +
            '}';
    }
}
