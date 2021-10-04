package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;

@RegisterForReflection
public class ValueAtRiskResult {

    private String id;
    private Double valueAtRisk;
    private LocalDateTime valueAtRiskAsOf;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValueAtRisk() {
        return valueAtRisk;
    }

    public void setValueAtRisk(Double valueAtRisk) {
        this.valueAtRisk = valueAtRisk;
    }

    public LocalDateTime getValueAtRiskAsOf() {
        return valueAtRiskAsOf;
    }

    public void setValueAtRiskAsOf(LocalDateTime valueAtRiskAsOf) {
        this.valueAtRiskAsOf = valueAtRiskAsOf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((valueAtRisk == null) ? 0 : valueAtRisk.hashCode());
        result = prime * result + ((valueAtRiskAsOf == null) ? 0 : valueAtRiskAsOf.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValueAtRiskResult other = (ValueAtRiskResult) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (valueAtRisk == null) {
            if (other.valueAtRisk != null)
                return false;
        } else if (!valueAtRisk.equals(other.valueAtRisk))
            return false;
        if (valueAtRiskAsOf == null) {
            if (other.valueAtRiskAsOf != null)
                return false;
        } else if (!valueAtRiskAsOf.equals(other.valueAtRiskAsOf))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ValueAtRiskResult [id=" + id + ", valueAtRisk=" + valueAtRisk + ", valueAtRiskAsOf=" + valueAtRiskAsOf
                + "]";
    }

}
