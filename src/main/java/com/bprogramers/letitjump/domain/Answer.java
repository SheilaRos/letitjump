package com.bprogramers.letitjump.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Answer.
 */
@Entity
@Table(name = "answer")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "text")
    private String text;

    @Column(name = "liked")
    private Integer liked;

    @Column(name = "disliked")
    private Integer disliked;

    @ManyToOne
    private ForumEntry forumEntry;

    @ManyToOne
    private Answer answer;

    @OneToMany(mappedBy = "answer")
    @JsonIgnore
    private Set<Answer> answers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public Answer summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public Answer text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLiked() {
        return liked;
    }

    public Answer liked(Integer liked) {
        this.liked = liked;
        return this;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }

    public Integer getDisliked() {
        return disliked;
    }

    public Answer disliked(Integer disliked) {
        this.disliked = disliked;
        return this;
    }

    public void setDisliked(Integer disliked) {
        this.disliked = disliked;
    }

    public ForumEntry getForumEntry() {
        return forumEntry;
    }

    public Answer forumEntry(ForumEntry forumEntry) {
        this.forumEntry = forumEntry;
        return this;
    }

    public void setForumEntry(ForumEntry forumEntry) {
        this.forumEntry = forumEntry;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Answer answer(Answer answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public Answer answers(Set<Answer> answers) {
        this.answers = answers;
        return this;
    }

    public Answer addAnswer(Answer answer) {
        answers.add(answer);
        answer.setAnswer(this);
        return this;
    }

    public Answer removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setAnswer(null);
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
        Answer answer = (Answer) o;
        if (answer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + id +
            ", summary='" + summary + "'" +
            ", text='" + text + "'" +
            ", liked='" + liked + "'" +
            ", disliked='" + disliked + "'" +
            '}';
    }
}
