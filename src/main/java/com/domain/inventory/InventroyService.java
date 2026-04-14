package com.domain.inventory;

import org.project.Item;
import java.time.LocalDate;
import java.util.List;

public class InventroyService {
    private InventroyRepository inventroyRepository = new InventroyRepository();

    public void addItem(String name, int qty, String category, int price, LocalDate expiryDate, int minStock){

        Item item = new Item(inventroyRepository.getNextId(), name, qty, category, price, expiryDate, minStock);
        inventroyRepository.save(item);
    }

    public String outputItem(int id, int cnt){
        Item item = inventroyRepository.findById(id);
        if(item == null){
            return "존재하지 않는 재고 번호 입니다.";
        }
        if(item.getProduct_quantity() < cnt) return "재고 개수가 부족합니다.";

        item.setProduct_quantity(item.getProduct_quantity() - cnt);

        if(item.getProduct_quantity() <= item.getProduct_minStock())
            return "출고 완료 >>>> %s %d개\n 재고 부족 경고 >>>> %s %d개 남음"
                    .formatted(item.getProduct_name(), cnt, item.getProduct_name(), item.getProduct_quantity());

        return "출고 완료 >>>> %s %d개".formatted(item.getProduct_name(), cnt);
    }

    public List<Item> getAll(){
        return inventroyRepository.getAll();
    }

    public String modifyItem(int id, String name, int qty){
        Item item = inventroyRepository.findById(id);
        if(item == null)  return "존재하지 않는 재고 번호 입니다.";
        item.setProduct_name(name);
        item.setProduct_quantity(qty);

        inventroyRepository.save(item);

        return "수정 완료 >>>> %s %d개".formatted(item.getProduct_name(), item.getProduct_quantity());
    }

    public String deleteItem(int id){
        Item item = inventroyRepository.findById(id);
        if(item == null) return "존재하지 않는 재고 번호 입니다.";
        inventroyRepository.remove(item);
        return "삭제 완료 >>>> %s".formatted(item.getProduct_name());
    }

    public List<Item> getExpiryWarning(){
        return inventroyRepository.getExpiryWarning();
    }

    public List<Item> getMinStockWarning(){
        return inventroyRepository.getMinStockWarning();
    }

    public void saveToFile(){
       inventroyRepository.saveFile();
    }

    public void loadFromFile(){
       inventroyRepository.loadFile();
    }
}
