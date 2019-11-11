package org.osrs.min.api.wrappers;

public class GrandExchangeOffer {
    private final org.osrs.min.api.accessors.GrandExchangeOffer accessor;
    private final int index;

    public GrandExchangeOffer(org.osrs.min.api.accessors.GrandExchangeOffer accessor, int index) {
        this.accessor = accessor;
        this.index = index;

    }

    public boolean isEmpty() {
        return this.getState() == 0;
    }

    public OfferType getType() {
        if (this.getState() == 0) {
            return OfferType.EMPTY;
        }
        if ((this.getState() & 8) != 8) return OfferType.BUY;
        return OfferType.SELL;
    }

    public byte getState() {
        if (!hasAccessor())
            return (byte) -1;
        return accessor.getState();
    }

    public int getItemId() {
        if (!hasAccessor())
            return -1;
        return accessor.getId();
    }

    public int getItemPrice() {
        if (!hasAccessor())
            return -1;
        return accessor.getUnitPrice();
    }

    public int getItemQuantity() {
        if (!hasAccessor())
            return -1;
        return accessor.getCurrentQuantity();
    }

    public int getSpent() {
        if (!hasAccessor())
            return -1;
        return accessor.getCurrentPrice();
    }

    public int getIndex() {
        return index;
    }

    protected boolean hasAccessor() {
        return accessor != null;
    }

    public enum OfferType {
        BUY("Buy", 0),
        SELL("Sell", 1),
        EMPTY("Empty", 2);
        private String name;
        private int number;

        OfferType(String name, int number) {
            this.name = name;
            this.number = number;
        }
    }
}
