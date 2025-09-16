package com.evconnect.models;

public class BookingRequest {
    private String customerNic;
    private String chargerCode;
    private String slotId;

    public BookingRequest(String customerNic, String chargerCode, String slotId) {
        this.customerNic = customerNic;
        this.chargerCode = chargerCode;
        this.slotId = slotId;
    }

    public String getCustomerNic() {
        return customerNic;
    }

    public void setCustomerNic(String customerNic) {
        this.customerNic = customerNic;
    }

    public String getChargerCode() {
        return chargerCode;
    }

    public void setChargerCode(String chargerCode) {
        this.chargerCode = chargerCode;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }
}
