package roster.domain.model;

import lombok.Data;

import java.util.Objects;

public @Data class RosterEntity<T extends RosterEntityId> {

    private T entityId;

    protected RosterEntity(T entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RosterEntity<?> that = (RosterEntity<?>) o;
        return Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), entityId);
    }
}
