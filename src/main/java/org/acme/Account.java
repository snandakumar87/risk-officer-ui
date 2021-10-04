package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class Account {

    private String accountId;
    private List<Holding> holdings;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<Holding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Holding> holdings) {
        this.holdings = holdings;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
        result = prime * result + ((holdings == null) ? 0 : holdings.hashCode());
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
        Account other = (Account) obj;
        if (accountId == null) {
            if (other.accountId != null)
                return false;
        } else if (!accountId.equals(other.accountId))
            return false;
        if (holdings == null) {
            if (other.holdings != null)
                return false;
        } else if (!holdings.equals(other.holdings))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", holdings=" + holdings + "]";
    }

}
