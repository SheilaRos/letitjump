package com.bprogramers.letitjump.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A UserCustomAtributes.
 */
@Entity
@Table(name = "user_custom_atributes")
public class UserCustomAtributes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "money_game")
    private Long moneyGame;

    @Column(name = "money_premium")
    private Long moneyPremium;

    @Column(name = "score")
    private Long score;

    @Column(name = "sex")
    private String sex;

    @NotNull
    @Min(value = 1)
    @Column(name = "level", nullable = false)
    private Integer level;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public UserCustomAtributes birthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Long getMoneyGame() {
        return moneyGame;
    }

    public UserCustomAtributes moneyGame(Long moneyGame) {
        this.moneyGame = moneyGame;
        return this;
    }

    public void setMoneyGame(Long moneyGame) {
        this.moneyGame = moneyGame;
    }

    public Long getMoneyPremium() {
        return moneyPremium;
    }

    public UserCustomAtributes moneyPremium(Long moneyPremium) {
        this.moneyPremium = moneyPremium;
        return this;
    }

    public void setMoneyPremium(Long moneyPremium) {
        this.moneyPremium = moneyPremium;
    }

    public Long getScore() {
        return score;
    }

    public UserCustomAtributes score(Long score) {
        this.score = score;
        return this;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getSex() {
        return sex;
    }

    public UserCustomAtributes sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getLevel() {
        return level;
    }

    public UserCustomAtributes level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public User getUser() {
        return user;
    }

    public UserCustomAtributes user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCustomAtributes userCustomAtributes = (UserCustomAtributes) o;
        if (userCustomAtributes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userCustomAtributes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserCustomAtributes{" +
            "id=" + id +
            ", birthday='" + birthday + "'" +
            ", moneyGame='" + moneyGame + "'" +
            ", moneyPremium='" + moneyPremium + "'" +
            ", score='" + score + "'" +
            ", sex='" + sex + "'" +
            ", level='" + level + "'" +
            '}';
    }
}
