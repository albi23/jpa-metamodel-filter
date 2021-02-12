package com.examples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "forum_topic")
public class ForumTopic extends BaseEntity {

    private String title;
    private String creatorUserName;
    private String hasztags;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "forum_topic_likes", joinColumns = {@JoinColumn(name = "id")})
    @Column(name = "user_id")
    private List<UUID> favorites = new ArrayList<>();

    @JsonIgnore
    @Column(name = "forum_category_id", insertable = false, updatable = false)
    private UUID forumCategoryId;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_category_id", referencedColumnName = "id")
    private ForumCategory forumCategory;

    public ForumTopic() {
    }

    public ForumTopic(String title, String creatorUserName, String hasztags, ForumCategory forumCategory) {
        this.title = title;
        this.creatorUserName = creatorUserName;
        this.hasztags = hasztags;
        this.forumCategory = forumCategory;
    }

    /**
     * Gets title
     *
     * @return value of title field
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets <code>ForumTopic</code> title value
     *
     * @param title - set new value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets creatorUserName
     *
     * @return value of creatorUserName field
     */
    public String getCreatorUserName() {
        return creatorUserName;
    }

    /**
     * Sets <code>ForumTopic</code> creatorUserName value
     *
     * @param creatorUserName - set new value of creatorUserName
     */
    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    /**
     * Gets hasztags
     *
     * @return value of hasztags field
     */
    public String getHasztags() {
        return hasztags;
    }

    /**
     * Sets <code>ForumTopic</code> hasztags value
     *
     * @param hasztags - set new value of hasztags
     */
    public void setHasztags(String hasztags) {
        this.hasztags = hasztags;
    }

    /**
     * Gets favoritesMap
     *
     * @return value of favoritesMap field
     */
    public List<UUID> getFavorites() {
        return favorites;
    }

    /**
     * Sets <code>ForumTopic</code> favoritesMap value
     *
     * @param favorites - set new value of favoritesMap
     */
    public void setFavorites(List<UUID> favorites) {
        this.favorites = favorites;
    }

    /**
     * Gets forumCategory
     *
     * @return value of forumCategory field
     */
    public ForumCategory getForumCategory() {
        return forumCategory;
    }

    /**
     * Sets <code>ForumTopic</code> forumCategory value
     *
     * @param forumCategory - set new value of forumCategory
     */
    public void setForumCategory(ForumCategory forumCategory) {
        this.forumCategory = forumCategory;
    }
}
