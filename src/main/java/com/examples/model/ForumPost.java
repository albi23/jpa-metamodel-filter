package com.examples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

/**
 * The class responsible for creating the forum_post table and mapping data to the form
 * of ForumPost objects
 */
@Entity
@Table(name = "forum_post")
public class ForumPost extends BaseEntity {

    private String text;

    @JsonIgnore
    @Column(name = "forum_topic_id", insertable = false, updatable = false)
    private UUID forumTopicId;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_topic_id", referencedColumnName = "id")
    private ForumTopic forumTopic;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person postCreator;

    public ForumPost() {
    }

    public ForumPost(Person postCreator, String text, ForumTopic forumTopic) {
        this.postCreator = postCreator;
        this.text = text;
        this.forumTopic = forumTopic;
    }

    /**
     * Gets text
     *
     * @return value of text field
     */
    public String getText() {
        return text;
    }

    /**
     * Sets <code>ForumPost</code> text value
     *
     * @param text - set new value of text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets creator
     *
     * @return value of creator field
     */
    public Person getPostCreator() {
        return postCreator;
    }

    /**
     * Sets <code>ForumPost</code> creator value
     *
     * @param postCreator - set new value of creator
     */
    public void setPostCreator(Person postCreator) {
        this.postCreator = postCreator;
    }

    /**
     * Gets forumTopicId
     *
     * @return value of forumTopicId field
     */
    public UUID getForumTopicId() {
        return forumTopicId;
    }

    /**
     * Sets <code>ForumPost</code> forumTopicId value
     *
     * @param forumTopicId - set new value of forumTopicId
     */
    public void setForumTopicId(UUID forumTopicId) {
        this.forumTopicId = forumTopicId;
    }

    /**
     * Gets forumTopic
     *
     * @return value of forumTopic field
     */
    public ForumTopic getForumTopic() {
        return forumTopic;
    }

    /**
     * Sets <code>ForumPost</code> forumTopic value
     *
     * @param forumTopic - set new value of forumTopic
     */
    public void setForumTopic(ForumTopic forumTopic) {
        this.forumTopic = forumTopic;
    }
}
