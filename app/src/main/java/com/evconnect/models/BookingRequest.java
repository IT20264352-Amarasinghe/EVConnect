package com.evconnect.models;

public class BookingRequest {
    private String customerNic;
    private String chargerId;
    private String slotId;

    public BookingRequest(String customerNic, String chargerId, String slotId) {
        this.customerNic = customerNic;
        this.chargerId = chargerId;
        this.slotId = slotId;
    }

    public String getCustomerNic() {
        return customerNic;
    }

    public void setCustomerNic(String customerNic) {
        this.customerNic = customerNic;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }
}
