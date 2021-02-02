package com.examples.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @Column(columnDefinition ="uuid")
    private UUID id = UUID.randomUUID();

    @Version
    private int version;

    public BaseEntity() {
    }

    public boolean equals(Object object) {
        return object instanceof BaseEntity && this.id.equals(((BaseEntity) object).id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    /**
     * Gets id
     *
     * @return value of id field
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets <code>BaseEntity</code> id value
     *
     * @param id - set new value of id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets version
     *
     * @return value of version field
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets <code>BaseEntity</code> version value
     *
     * @param version - set new value of version
     */
    public void setVersion(int version) {
        this.version = version;
    }
}
