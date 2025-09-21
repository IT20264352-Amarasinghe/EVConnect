package com.evconnect.models;

public class Booking {
    private String id;
    private String customerNic;
    private Charger charger;
    private Slot slot;
    private String createdAt;
    private String status;

    public static class Charger {
        private String id;
        private String code;
        private String location;

        // Getters and setters
        public String getCode() { return code; }
        public String getLocation() { return location; }
    }

    public static class Slot {
        private String id;
        private String date;
        private String startTime;
        private String endTime;
        private String status;

        // Getters and setters
        public String getDate() { return date; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getStatus() { return status; }
    }

    // Getters
    public Charger getCharger() { return charger; }
    public Slot getSlot() { return slot; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
}

