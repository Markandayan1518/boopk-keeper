package com.bookkeeper.model;

/**
 * Represents a system alert.
 */
public class Alert {
    private String type;       // e.g., "LargeAdvance", "OverduePayment"
    private String entityId;   // farmerId or saleId
    private String message;

    private Alert(Builder builder) {
        this.type = builder.type;
        this.entityId = builder.entityId;
        this.message = builder.message;
    }

    public static Builder from(Alert alert) {
        return builder()
            .withType(alert.getType())
            .withEntityId(alert.getEntityId())
            .withMessage(alert.getMessage());
    }

    // Getters only - removing setters for immutability
    public String getType() { return type; }
    public String getEntityId() { return entityId; }
    public String getMessage() { return message; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private String entityId;
        private String message;

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withEntityId(String entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Alert build() {
            return new Alert(this);
        }
    }
}
