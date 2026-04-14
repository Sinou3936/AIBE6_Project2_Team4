package org.project;

import com.domain.inventory.InventroyController;
import com.domain.system.SystemController;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private Rq rq;
    private boolean running = true;
    private SystemController systemController = new SystemController();
    private final InventroyController inventoryController = new InventroyController(scanner);

    void run() throws FileNotFoundException {

        System.out.println("==== 자바 CLI 서비스 만들기 ====");

        systemController.bootSystem();


        while (running) {
            System.out.print("명령어 입력  : ");
            String cmd = scanner.nextLine().trim();

            rq = new Rq(cmd);

            switch (rq.getCmd()) {
                case "입고" -> inventoryController.inputItem();
                case "출고" -> inventoryController.outputItem();
                case "목록" -> inventoryController.printList();
                case "저장" -> inventoryController.saveToFile();
                case "로드" -> inventoryController.loadFromFile();
                case "수정" -> inventoryController.modifyItem(rq);
                case "삭제" -> inventoryController.deleteItem(rq);
                case "유통기한" -> inventoryController.checkExpiry();
                case "경고" -> inventoryController.checkMinStock();
                case "종료" -> {
                    systemController.exit(running);
                    return;
                }
                default -> System.out.println("알 수 없는 명령입니다.");
            }
        }
    }







}
