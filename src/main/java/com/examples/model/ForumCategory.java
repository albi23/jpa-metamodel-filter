package com.examples.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The class responsible for creating the ForumCategory table and mapping data to the form
 * of ForumCategory objects
 */
@Entity
@Table(name = "forum_category")
public class  ForumCategory extends BaseEntity {

    private String translationName;
    private String description;
    private String icon;

    public ForumCategory() {
    }

    public ForumCategory(String translationName, String icon, String description) {
        this.translationName = translationName;
        this.icon = icon;
        this.description = description;
    }

    /**
     * Gets translationName
     *
     * @return value of translationName field
     */
    public String getTranslationName() {
        return translationName;
    }

    /**
     * Sets <code>ForumCategory</code> translationName value
     *
     * @param translationName - set new value of translationName
     */
    public void setTranslationName(String translationName) {
        this.translationName = translationName;
    }

    /**
     * Gets icon
     *
     * @return value of icon field
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets <code>ForumCategory</code> icon value
     *
     * @param icon - set new value of icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Gets description
     *
     * @return value of description field
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets <code>ForumCategory</code> description value
     *
     * @param description - set new value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
