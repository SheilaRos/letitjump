package com.bprogramers.letitjump.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Level.
 */
@Entity
@Table(name = "level")
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(unique = true)
    private Map map;

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    private Set<UserCustomAtributes> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Level name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getMap() {
        return map;
    }

    public Level map(Map map) {
        this.map = map;
        return this;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Set<UserCustomAtributes> getUsers() {
        return users;
    }

    public Level users(Set<UserCustomAtributes> userCustomAtributes) {
        this.users = userCustomAtributes;
        return this;
    }

    public Level addUser(UserCustomAtributes userCustomAtributes) {
        users.add(userCustomAtributes);
        userCustomAtributes.setLevel(this);
        return this;
    }

    public Level removeUser(UserCustomAtributes userCustomAtributes) {
        users.remove(userCustomAtributes);
        userCustomAtributes.setLevel(null);
        return this;
    }

    public void setUsers(Set<UserCustomAtributes> userCustomAtributes) {
        this.users = userCustomAtributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Level level = (Level) o;
        if (level.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, level.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Level{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
