package kr.kro.majab.store;


public enum StoreStatus {
    OPEN("영업중"),
    CLOSE("영업 마감"),
    SUSPENDED("일시 중지");

    private final String description;

    StoreStatus(String description) {
        this.description = description;
    }
}
