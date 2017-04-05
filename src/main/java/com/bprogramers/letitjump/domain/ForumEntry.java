package com.bprogramers.letitjump.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ForumEntry.
 */
@Entity
@Table(name = "forum_entry")
public class ForumEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "forumEntry")
    @JsonIgnore
    private Set<Answer> answers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ForumEntry title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public ForumEntry text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public ForumEntry user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public ForumEntry answers(Set<Answer> answers) {
        this.answers = answers;
        return this;
    }

    public ForumEntry addAnswer(Answer answer) {
        answers.add(answer);
        answer.setForumEntry(this);
        return this;
    }

    public ForumEntry removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setForumEntry(null);
        return this;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ForumEntry forumEntry = (ForumEntry) o;
        if (forumEntry.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, forumEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ForumEntry{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", text='" + text + "'" +
            '}';
    }
}
