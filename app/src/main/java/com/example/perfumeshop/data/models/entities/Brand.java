package com.example.perfumeshop.data.models.entities;

public class Brand {
    private String _id;
    private String brandName;

    public Brand() {}

    public Brand(String _id, String brandName) {
        this._id = _id;
        this.brandName = brandName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return brandName;
    }
}
