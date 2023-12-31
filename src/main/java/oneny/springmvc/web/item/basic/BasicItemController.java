package oneny.springmvc.web.item.basic;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oneny.springmvc.domain.Item;
import oneny.springmvc.domain.ItemRepository;
import oneny.springmvc.domain.item.DeliveryCode;
import oneny.springmvc.domain.item.ItemType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/basic/items/{itemId}";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "basic/addForm";
  }

  /**
   * ReidrectAttributes
   */
  @PostMapping("/add")
  public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
    log.info("item.open={}", item.getOpen());
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);

    return "redirect:/basic/items/{itemId}";
  }

  @ModelAttribute("regions")
  public Map<String, String> regions() {
    Map<String, String> regions = new LinkedHashMap<>();
    regions.put("SEOUL", "서울");
    regions.put("BUSAN", "부산");
    regions.put("JEJU", "제주");
    return regions;
  }

  @ModelAttribute("itemTypes")
  public ItemType[] itemTypes() {
    return ItemType.values();
  }

  @ModelAttribute("deliveryCodes")
  public List<DeliveryCode> deliveryCodes() {
    List<DeliveryCode> deliveryCodes = new ArrayList<>();
    deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
    deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
    deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));

    return deliveryCodes;
  }

  /**
   * PRG - Post/Redirect/Get
   * "redirect:/basic/items/" + item.getId() 주의
   * redirect 에서 + item.getId() 처럼 URL 에 변수를 더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험하다.
   * 다음에 위 V6 의 RedirectAttributes 를 사용하자.
   */
//  @PostMapping("/add")
  public String addItemV5(Item item) {
    itemRepository.save(item);
    return "redirect:/basic/items/" + item.getId();
  }

  /**
   * @ModelAttribute 자체 생략 가능
   * model.addAttribute(item); 자동 추가
   */
//  @PostMapping("/add")
  public String addItemV4(Item item) {
    itemRepository.save(item);
    return "basic/item";
  }

  /**
   * @ModelAttribute name 생략 가능
   * model.addAttribute(item); 자동 추가, 생략 가능
   * 생략 시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
   */
//  @PostMapping("/add")
  public String addItemV3(@ModelAttribute Item item) {
    itemRepository.save(item);
    return "basic/item";
  }


  /**
   * @ModelAttribute("item") Item item
   * model.addAttribute("item", item); 자동 추가
   */
//  @PostMapping("/add")
  public String addItemV2(@ModelAttribute("item") Item item) {
    itemRepository.save(item);
    // model.addAttribute("item", item); 자동 추가, 생략 가능
    return "basic/item";
  }

  //  @PostMapping("/add")
  public String addItemV1(@RequestParam String itemName,
                          @RequestParam int price,
                          @RequestParam Integer quantity,
                          Model model) {
    Item item = new Item();
    item.setItemName(itemName);
    item.setPrice(price);
    item.setQuantity(quantity);

    itemRepository.save(item);

    model.addAttribute("item", item);

    return "basic/item";
  }

  /**
   * 테스트용 데이터 추가
   */
  @PostConstruct
  public void init() {
    itemRepository.save(new Item("testA", 10000, 10));
    itemRepository.save(new Item("testB", 20000, 20));
  }
}
