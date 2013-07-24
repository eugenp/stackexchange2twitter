package org.keyval.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.common.persistence.IEntity;

@Entity
public class KeyVal implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "KEYVALID")
    private long id;

    @Column(unique = true, nullable = false)
    @NotNull
    private String key;

    @Column(unique = false, nullable = false, length = 2048)
    @NotNull
    private String value;

    public KeyVal() {
        super();
    }

    public KeyVal(final String keyToSet, final String valueToSet) {
        super();
        key = keyToSet;
        value = valueToSet;
    }

    // API

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(final long idToSet) {
        id = idToSet;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String keyToSet) {
        key = keyToSet;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String valueToSet) {
        value = valueToSet;
    }

    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyVal other = (KeyVal) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("KeyVal [key=").append(key).append(", value=").append(value).append("]");
        return builder.toString();
    }

}
