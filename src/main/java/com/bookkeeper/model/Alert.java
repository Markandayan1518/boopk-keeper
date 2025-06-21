package com.bookkeeper.model;

/**
 * Represents a system alert.
 */
public class Alert {
    private String type;       // e.g., "LargeAdvance", "OverduePayment"
    private String entityId;   // farmerId or saleId
    private String message;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
