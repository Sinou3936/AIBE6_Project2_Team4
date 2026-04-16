package com.domain.inventory;


import org.project.Item;
import org.project.Rq;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class InventroyController {

    private final Scanner scanner;
    private InventroyService inventoryService = new InventroyService();

    public InventroyController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void inputItem(){
        System.out.print("입고 대상: ");
        String inputItem = scanner.nextLine().trim();
        System.out.print("입고 개수: ");
        String inputItemCnt = scanner.nextLine().trim();
        System.out.print("카테고리: ");
        String inputCategory = scanner.nextLine().trim();
        System.out.print("단가: ");
        String inputPrice = scanner.nextLine().trim();
        System.out.print("유통기한 (예: 2026-12-31): ");
        String inputExpiryDate = scanner.nextLine().trim();
        LocalDate expiryDate = LocalDate.parse(inputExpiryDate);
        System.out.print("최소 재고 기준: ");
        String inputMinStock = scanner.nextLine().trim();

        System.out.println("입고 완료 >>>> %s %s개 %s %s원 %s".formatted(inputItem,inputItemCnt,inputCategory,inputPrice,expiryDate.toString(),inputMinStock));
        inventoryService.addItem(
                inputItem,
                Integer.parseInt(inputItemCnt),
                inputCategory,
                Integer.parseInt(inputPrice),
                expiryDate,
                Integer.parseInt(inputMinStock)
        );
    }

    public void outputItem(){
        System.out.println("출고_대상_목록");
        System.out.println("재고_번호 | 재고_이름 | 재고_개수 | 카테고리 | 재고_가격 | 유통기한 | 최소_재고_기준 | 유통기한_상태");
        inventoryService.getAll()
                .forEach(item -> {
                    long dDay = ChronoUnit.DAYS.between(LocalDate.now(), item.getProduct_expiryDate());
                    String expiryStatus = dDay < 0 ? "유통기한 지남" : "D-%d".formatted(dDay);
                    System.out.println("%05d | %s | %d | %s | %d | %s | %d %s"
                            .formatted(item.getId(), item.getProduct_name(), item.getProduct_quantity()
                                    ,item.getProduct_category(), item.getProduct_price(), item.getProduct_expiryDate(), item.getProduct_minStock()
                                    ,expiryStatus

                            ));
                });
        System.out.print("출고 대상 번호: ");
        String outputItemId = scanner.nextLine().trim();
        System.out.print("출고 개수: ");
        String outputItemCnt = scanner.nextLine().trim();

        System.out.println(inventoryService.outputItem(Integer.parseInt(outputItemId), Integer.parseInt(outputItemCnt)));
    }

    public void printList(){
        System.out.println("재고_목록");
        System.out.println("재고_번호 | 재고_이름 | 재고_개수 | 카테고리 | 재고_가격 | 유통기한 | 최소_재고_기준 | 유통기한_상태");

        int pageSize = 10;
        List<Item> all = inventoryService.getAll();
        int totalPage = (int) Math.ceil((double) all.size() / pageSize);
        int currentPage = 0;

        while (true){
            int start = currentPage * pageSize;;
            int end = Math.min(start + pageSize, all.size());

            all.subList(start,end).forEach(item -> {
                long dDay = ChronoUnit.DAYS.between(LocalDate.now(), item.getProduct_expiryDate());
                String expiryStatus = dDay < 0 ? "유통기한 지남" : "D-%d".formatted(dDay);
                System.out.println("%05d | %s | %d | %s | %d | %s | %d %s"
                        .formatted(item.getId(), item.getProduct_name(), item.getProduct_quantity(),
                                item.getProduct_category(), item.getProduct_price(), item.getProduct_expiryDate(),
                                item.getProduct_minStock(), expiryStatus));
            });

            System.out.printf("페이지 %d/%d%n", currentPage + 1, totalPage);
            System.out.print("명령 (n: 다음 페이지, p: 이전 페이지, q: 종료): ");
            String command = scanner.nextLine().trim();

            if(command.equals("q")) break;
            if(command.equals("n") && currentPage < totalPage -1) currentPage++;
            if(command.equals("p") && currentPage > 0 ) currentPage--;
        }
    }

    public void saveToFile(){
        System.out.println("재고 목록 저장...");
        inventoryService.saveToFile();
        System.out.println("재고 목록 저장 완료...");
    }

    public void loadFromFile(){
        System.out.println("재고 목록 로드...");
        inventoryService.loadFromFile();
        System.out.println("재고 목록 로드 완료...");
    }

    public void modifyItem(Rq rq){
        int modifyId = rq.getIntParam("id","0");

        if(modifyId == 0) {
            System.out.println("사용법: 수정?id=번호&name=이름&qty=개수");
            return;
        }

        String modifyName = rq.getParams("name","");
        int modifyCnt = rq.getIntParam("qty","0");

        System.out.println(inventoryService.modifyItem(modifyId, modifyName, modifyCnt));

    }

    public void deleteItem(Rq rq){
        int deletedId = rq.getIntParam("id","0");
        if(deletedId == 0) {
            System.out.println("사용법: 삭제?id=번호");
            return;
        }
        System.out.println(inventoryService.deleteItem(deletedId));
    }

    public void checkExpiry() {
        System.out.println("유통기한_임박_재고_목록");
        System.out.println("재고_번호 | 재고_이름 | 재고_개수 | 유통기한 | 유통기한_상태");

        inventoryService.getExpiryWarning().forEach(item -> {
            long dDay = ChronoUnit.DAYS.between(LocalDate.now(), item.getProduct_expiryDate());
            String expiryStatus = dDay < 0 ? "유통기한 지남" : "D-%d".formatted(dDay);
            System.out.println("%05d | %s | %d | %s | %s"
                    .formatted(item.getId(), item.getProduct_name(), item.getProduct_quantity(),
                            item.getProduct_expiryDate(), expiryStatus));
        });
    }

    public void checkMinStock() {
        System.out.println("재고_부족_경고_목록");
        System.out.println("재고_번호 | 재고_이름 | 재고_개수 | 최소_재고_기준");

        inventoryService.getMinStockWarning().forEach(item -> System.out.println("%05d | %s | %d | %d"
                .formatted(item.getId(), item.getProduct_name(), item.getProduct_quantity(), item.getProduct_minStock())));
    }
}
