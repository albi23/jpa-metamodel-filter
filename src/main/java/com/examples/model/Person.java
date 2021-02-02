package com.examples.model;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "person", indexes = {@Index(name = "person_index", columnList = "userName")})
public class Person {

    private String name;
    private String surname;
    private String userName;

    public Person() {
    }

    /**
     * Gets name
     *
     * @return value of name field
     */
    public String getName() {
        return name;
    }

    /**
     * Sets <code>Person</code> name value
     *
     * @param name - set new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets surname
     *
     * @return value of surname field
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets <code>Person</code> surname value
     *
     * @param surname - set new value of surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets userName
     *
     * @return value of userName field
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets <code>Person</code> userName value
     *
     * @param userName - set new value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
