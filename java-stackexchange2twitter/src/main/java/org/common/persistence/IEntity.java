package org.common.persistence;

import java.io.Serializable;

public interface IEntity extends Serializable {

    long getId();

    void setId(long id);

}
