package com.bprogramers.letitjump.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PowerUp.
 */
@Entity
@Table(name = "power_up")
public class PowerUp implements Serializable {

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
    @Column(name = "time", nullable = false)
    private Integer time;

    @NotNull
    @Column(name = "attr", nullable = false)
    private String attr;

    @NotNull
    @Column(name = "attr_value", nullable = false)
    private Integer attrValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PowerUp name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPriceGame() {
        return priceGame;
    }

    public PowerUp priceGame(Long priceGame) {
        this.priceGame = priceGame;
        return this;
    }

    public void setPriceGame(Long priceGame) {
        this.priceGame = priceGame;
    }

    public Long getPricePremium() {
        return pricePremium;
    }

    public PowerUp pricePremium(Long pricePremium) {
        this.pricePremium = pricePremium;
        return this;
    }

    public void setPricePremium(Long pricePremium) {
        this.pricePremium = pricePremium;
    }

    public String getSplashArt() {
        return splashArt;
    }

    public PowerUp splashArt(String splashArt) {
        this.splashArt = splashArt;
        return this;
    }

    public void setSplashArt(String splashArt) {
        this.splashArt = splashArt;
    }

    public Integer getTime() {
        return time;
    }

    public PowerUp time(Integer time) {
        this.time = time;
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getAttr() {
        return attr;
    }

    public PowerUp attr(String attr) {
        this.attr = attr;
        return this;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public Integer getAttrValue() {
        return attrValue;
    }

    public PowerUp attrValue(Integer attrValue) {
        this.attrValue = attrValue;
        return this;
    }

    public void setAttrValue(Integer attrValue) {
        this.attrValue = attrValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PowerUp powerUp = (PowerUp) o;
        if (powerUp.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, powerUp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PowerUp{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", priceGame='" + priceGame + "'" +
            ", pricePremium='" + pricePremium + "'" +
            ", splashArt='" + splashArt + "'" +
            ", time='" + time + "'" +
            ", attr='" + attr + "'" +
            ", attrValue='" + attrValue + "'" +
            '}';
    }
}
