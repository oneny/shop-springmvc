package oneny.springmvc.domain;

import lombok.Getter;
import lombok.Setter;
import oneny.springmvc.domain.item.ItemType;

import java.util.List;

@Getter @Setter
public class Item {

  private Long id;
  private String itemName;
  private Integer price;
  private Integer quantity;

  private Boolean open; // 판매 여부
  private List<String> regions; // 등록 지역
  private ItemType itemType; // 상품 종료
  private String deliveryCode; // 배송 방식

  public Item() {
  }

  public Item(String itemName, Integer price, Integer quantity) {
    this.id = id;
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
  }
}
