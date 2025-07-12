package com.example.perfumeshop.data.models.entities;

public class CartItem {
    private String perfumeId;
    private String perfumeName;
    private String brandName;
    private String imageUrl;
    private long price;
    private int volume;
    private String targetAudience;
    private int quantity;

    public CartItem() {}

    public CartItem(String perfumeId, String perfumeName, String brandName, String imageUrl, 
                   long price, int volume, String targetAudience, int quantity) {
        this.perfumeId = perfumeId;
        this.perfumeName = perfumeName;
        this.brandName = brandName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.volume = volume;
        this.targetAudience = targetAudience;
        this.quantity = quantity;
    }

    // Create from Perfume object
    public static CartItem fromPerfume(Perfume perfume) {
        return new CartItem(
                perfume.get_id(),
                perfume.getPerfumeName(),
                perfume.getBrand() != null ? perfume.getBrand().getBrandName() : "Unknown",
                perfume.getUri(),
                perfume.getPrice(),
                perfume.getVolume(),
                perfume.getTargetAudience(),
                1 // Default quantity
        );
    }

    // Getters and Setters
    public String getPerfumeId() {
        return perfumeId;
    }

    public void setPerfumeId(String perfumeId) {
        this.perfumeId = perfumeId;
    }

    public String getPerfumeName() {
        return perfumeName;
    }

    public void setPerfumeName(String perfumeName) {
        this.perfumeName = perfumeName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFormattedPrice() {
        return String.format("%,d VND", price);
    }

    public String getFormattedTotalPrice() {
        return String.format("%,d VND", price * quantity);
    }
}
