package com.domain.inventory;

import org.project.Item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InventroyRepository {
    private ArrayList<Item> inventory = new ArrayList<>();
    private int nextId = 1;

    public void save(Item item)
    {
        Item existing = findById(item.getId());
        if(existing == null)
            inventory.add(item);
        else{
            existing.setProduct_name(item.getProduct_name());
            existing.setProduct_quantity(item.getProduct_quantity());
            existing.setProduct_category(item.getProduct_category());
            existing.setProduct_price(item.getProduct_price());
            existing.setProduct_expiryDate(item.getProduct_expiryDate());
            existing.setProduct_minStock(item.getProduct_minStock());
        }
    }

    public Item findById(int id){
        return inventory.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public void remove(Item item){
        inventory.remove(item);
    }

    public List<Item> getAll(){
        return inventory;
    }

    public void saveFile(){
        try(PrintWriter pw = new PrintWriter("inventory.txt")) {
            inventory.stream()
                    .forEach(item -> pw.println("%05d | %s | %d | %s | %d | %s | %d"
                            .formatted(item.getId(), item.getProduct_name(), item.getProduct_quantity()
                                    ,item.getProduct_category(), item.getProduct_price(), item.getProduct_expiryDate(), item.getProduct_minStock()
                            )));
            pw.flush();
        }catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
        }
    }
    public void loadFile(){
        inventory.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("inventory.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    int quantity = Integer.parseInt(parts[2].trim());
                    String category = parts[3].trim();
                    int price =Integer.parseInt(parts[4].trim());
                    LocalDate expiryDate = LocalDate.parse(parts[5].trim());
                    int minStock = Integer.parseInt(parts[6].trim());
                    inventory.add(new Item(id, name, quantity, category,price,expiryDate,minStock));
                    nextId = Math.max(nextId, id + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
        } catch (Exception e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다.");
        }
    }

    public int getNextId() {
        return nextId++;
    }

    public List<Item> getExpiryWarning(){
        return inventory.stream()
                .filter(item -> ChronoUnit.DAYS.between(LocalDate.now(), item.getProduct_expiryDate()) <= 7)
                .toList();
    }

    public List<Item> getMinStockWarning(){
        return inventory.stream()
                .filter(item -> item.getProduct_quantity() <= item.getProduct_minStock())
                .toList();
    }
}
