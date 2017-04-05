package com.bprogramers.letitjump.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Achievement.
 */
@Entity
@Table(name = "achievement")
public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "prize")
    private Long prize;

    @ManyToMany
    @JoinTable(name = "achievement_user",
               joinColumns = @JoinColumn(name="achievements_id", referencedColumnName="ID"),
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

    public Achievement name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrize() {
        return prize;
    }

    public Achievement prize(Long prize) {
        this.prize = prize;
        return this;
    }

    public void setPrize(Long prize) {
        this.prize = prize;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Achievement users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Achievement addUser(User user) {
        users.add(user);
        return this;
    }

    public Achievement removeUser(User user) {
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
        Achievement achievement = (Achievement) o;
        if (achievement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, achievement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Achievement{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", prize='" + prize + "'" +
            '}';
    }
}
