package com.evconnect.models;

public class Booking {
    private String id;
    private String customerNic;
    private Charger charger;
    private Slot slot;
    private String createdAt;
    private String updatedAt;
    private String status;

    public static class Charger {
        private String id;
        private String code;
        private String location;

        // Getters and setters
        public String getChargerId(){return id;}
        public String getCode() { return code; }
        public String getLocation() { return location; }

        public void setChargerId(String id) { this.id = id; }
        public void setCode(String code) { this.code = code; }
        public void setLocation(String location) { this.location = location; }


    }

    public static class Slot {
        private String id;
        private String date;
        private String startTime;
        private String endTime;
        private String status;

        // Getters and setters
        public String getSlotId(){return id;}
        public String getDate() { return date; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getStatus() { return status; }

        public void setSlotId(String id) { this.id = id; }
        public void setDate(String date) { this.date = date; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
        public void setStatus(String status) { this.status = status; }
    }

    // Getters
    public String getId(){return id;}
    public String getCustomerNic(){return customerNic;}
    public Charger getCharger() { return charger; }
    public Slot getSlot() { return slot; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public String getUpdatedAt(){return updatedAt;}

    public void setId(String id) { this.id = id; }
    public void setCustomerNic(){this.customerNic = customerNic;}
    public void setCharger(Charger charger) { this.charger = charger; }
    public void setSlot(Slot slot) { this.slot = slot; }
    public void setCustomerNic(String customerNic) { this.customerNic = customerNic; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public void setStatus(String status) { this.status = status; }

}

