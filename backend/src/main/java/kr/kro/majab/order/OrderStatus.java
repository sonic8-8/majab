package kr.kro.majab.order;

public enum OrderStatus {
    RESERVED("예약중"),
    CANCELED("취소"),
    COMPLETED("판매 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
