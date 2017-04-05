package com.bprogramers.letitjump.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserPowerUp.
 */
@Entity
@Table(name = "user_power_up")
public class UserPowerUp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private PowerUp powerUp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public UserPowerUp quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public UserPowerUp user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public UserPowerUp powerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
        return this;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPowerUp userPowerUp = (UserPowerUp) o;
        if (userPowerUp.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userPowerUp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserPowerUp{" +
            "id=" + id +
            ", quantity='" + quantity + "'" +
            '}';
    }
}
