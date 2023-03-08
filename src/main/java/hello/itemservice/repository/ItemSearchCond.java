package hello.itemservice.repository;

import lombok.Data;

/**
 * Dto package 위치
 * 1. itemservice.dto
 * 2. itemservice.repository
 *      : 가장 마지막에 호출되는 package 에 넣는다.
 *      : MVC 의 경우 Controller -> Service -> Repository 순으로 호출이 되기 때문에,
 *          Repository 까지 영향이 있는 Dto 일 경우 itemservice.repository 에 저장하는게
 */
@Data
public class ItemSearchCond {

    private String itemName;
    private Integer maxPrice;

    public ItemSearchCond() {
    }

    public ItemSearchCond(String itemName, Integer maxPrice) {
        this.itemName = itemName;
        this.maxPrice = maxPrice;
    }
}
