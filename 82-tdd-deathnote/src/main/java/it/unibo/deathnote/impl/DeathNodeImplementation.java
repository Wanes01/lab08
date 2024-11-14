package it.unibo.deathnote.impl;

import it.unibo.deathnote.api.DeathNote;
import java.util.*;

public class DeathNodeImplementation implements DeathNote {

    private final Map<String, DeathData> deaths = new HashMap<>();
    private String currentName;

    @Override
    public String getRule(int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Rule number out of boundaries");
        }
        return RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(String name) {
        if (name == null) {
            throw new NullPointerException("Can't write a null name on the death node");
        }
        this.currentName = name;
        this.deaths.put(name, new DeathData());
    }

    @Override
    public boolean writeDeathCause(String cause) {
        if (this.currentName == null || cause == null) {
            throw new IllegalStateException(cause == null
                    ? "A valid cause must be written"
                    : "can't specify the death cause of null");
        }
        return this.deaths.get(this.currentName).setCause(cause);
    }

    @Override
    public boolean writeDetails(String details) {
        if (this.currentName == null || details == null) {
            throw new IllegalStateException(details == null
                    ? "Valid details must be written"
                    : "can't specify the death details of null");
        }
        return this.deaths.get(this.currentName).setDetails(details);
    }

    @Override
    public String getDeathCause(String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Can't read the death cause of null");
        }
        return this.deaths.get(name).getCause();
    }

    @Override
    public String getDeathDetails(String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Can't read the death details of null");
        }
        return this.deaths.get(name).getDetails();
    }

    @Override
    public boolean isNameWritten(String name) {
        return this.deaths.containsKey(name);
    }

    private class DeathData {
        private static final long CAUSE_TIMER = 40;
        private static final long DETAILS_TIMER = CAUSE_TIMER + 6000;
        private static final String DEFAULT_DEATH_CAUSE = "heart attack";
        private static final String DEFAULT_DEATH_DETAILS = "";
        private final long deathTime;
        private String cause;
        private String details;

        public DeathData() {
            this.deathTime = System.currentTimeMillis();
            this.cause = DEFAULT_DEATH_CAUSE;
            this.details = DEFAULT_DEATH_DETAILS;
        }

        public boolean setCause(final String cause) {
            if (System.currentTimeMillis() - this.deathTime <= CAUSE_TIMER) {
                this.cause = cause;
                return true;
            }
            return false;
        }

        public boolean setDetails(final String details) {
            if (System.currentTimeMillis() - this.deathTime <= DETAILS_TIMER) {
                this.details = details;
                return true;
            }
            return false;
        }

        public String getCause() {
            return this.cause;
        }

        public String getDetails() {
            return this.details;
        }
    }

}