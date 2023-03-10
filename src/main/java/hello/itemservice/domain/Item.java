package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//pk 생성값을 데이터베이스에서 생성하는 IDENTITY 방식을 사용한다. ex. MySQL Auto increment
    private Long id;

    @Column(name = "item_name", length = 10)//name = "item_name" 은 jpa 에서 자동으로 camelcase 로 변환해 준다. 즉, 명시하지 않아도 된다.
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
