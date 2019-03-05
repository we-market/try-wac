package com.wemarket.wac.biz.dto;

/**
 * Created by justinli on 2019/3/5
 **/
public class OrderDTO {
    private String orderId;
    private String title;
    private int orderType;
    private String cargoName;
    private String cargoNum;
    private String originalPrice;
    private String discountPrice;
    private String discount;
    private String discription;
    private String photoPath;
    private String publisher;
    private String consumer;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoNum() {
        return cargoNum;
    }

    public void setCargoNum(String cargoNum) {
        this.cargoNum = cargoNum;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }
}
