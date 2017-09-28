package roster.domain.model;


import lombok.ToString;

import java.util.Objects;

/**
 * Base class for unique identifiers of the Entities
 */
public @ToString class RosterEntityId {

    protected String idValue;

    protected RosterEntityId(String idValue) {
        this.idValue = idValue;
    }

    public String getIdValue() {
        return idValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RosterEntityId that = (RosterEntityId) o;
        return Objects.equals(idValue, that.idValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idValue);
    }
}
