package org.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private ArrayList<Item> inventory;

    @BeforeEach
    void setUp() {
        inventory = new ArrayList<>();
        inventory.add(new Item(1, "콜라", 50, "음료", 1500, LocalDate.of(2026, 12, 31), 10));
        inventory.add(new Item(2, "과자", 5, "간식", 1000, LocalDate.of(2026, 4, 15), 10));
        inventory.add(new Item(3, "우유", 30, "냉장", 2000, LocalDate.of(2024, 1, 1), 5));
    }

    // ── Item 생성 ──────────────────────────────────────────

    @Test
    @DisplayName("아이템 정상 생성")
    void createItem() {
        Item item = new Item(1, "콜라", 50, "음료", 1500, LocalDate.of(2026, 12, 31), 10);
        assertEquals(1, item.getId());
        assertEquals("콜라", item.getProduct_name());
        assertEquals(50, item.getProduct_quantity());
        assertEquals("음료", item.getProduct_category());
        assertEquals(1500, item.getProduct_price());
        assertEquals(LocalDate.of(2026, 12, 31), item.getProduct_expiryDate());
        assertEquals(10, item.getProduct_minStock());
    }

    // ── 출고 ──────────────────────────────────────────────

    @Test
    @DisplayName("출고 후 수량 감소")
    void outputItem_quantityDecreased() {
        Item item = inventory.get(0); // 콜라 50개
        int outputCnt = 10;
        item.setProduct_quantity(item.getProduct_quantity() - outputCnt);
        assertEquals(40, item.getProduct_quantity());
    }

    @Test
    @DisplayName("출고 수량이 재고보다 많으면 재고 부족")
    void outputItem_insufficientStock() {
        Item item = inventory.get(0); // 콜라 50개
        int outputCnt = 100;
        assertTrue(item.getProduct_quantity() < outputCnt);
    }

    @Test
    @DisplayName("출고 후 최소 재고 이하 여부 확인")
    void outputItem_belowMinStock() {
        Item item = inventory.get(1); // 과자 5개, 최소재고 10개
        assertTrue(item.getProduct_quantity() <= item.getProduct_minStock());
    }

    // ── 유통기한 ───────────────────────────────────────────

    @Test
    @DisplayName("유통기한 7일 이내 항목 필터링")
    void checkExpiry_within7Days() {
        long count = inventory.stream()
                .filter(item -> {
                    long dDay = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), item.getProduct_expiryDate());
                    return dDay <= 7;
                })
                .count();
        // 과자(2026-04-15 = D-1), 우유(2024-01-01 = 유통기한 지남) → 2개
        assertEquals(2, count);
    }

    @Test
    @DisplayName("유통기한이 지난 항목 확인")
    void checkExpiry_expired() {
        Item item = inventory.get(2); // 우유 2024-01-01
        long dDay = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), item.getProduct_expiryDate());
        assertTrue(dDay < 0);
    }

    // ── 최소 재고 경고 ──────────────────────────────────────

    @Test
    @DisplayName("최소 재고 이하 항목 필터링")
    void checkMinStock() {
        long count = inventory.stream()
                .filter(item -> item.getProduct_quantity() <= item.getProduct_minStock())
                .count();
        // 과자(5 <= 10) → 1개
        assertEquals(1, count);
    }

    // ── 수정 ──────────────────────────────────────────────

    @Test
    @DisplayName("아이템 이름 수정")
    void modifyItem_name() {
        Item item = inventory.get(0);
        item.setProduct_name("펩시");
        assertEquals("펩시", item.getProduct_name());
    }

    @Test
    @DisplayName("아이템 수량 수정")
    void modifyItem_quantity() {
        Item item = inventory.get(0);
        item.setProduct_quantity(100);
        assertEquals(100, item.getProduct_quantity());
    }

    // ── 삭제 ──────────────────────────────────────────────

    @Test
    @DisplayName("아이템 삭제")
    void deleteItem() {
        Item item = inventory.stream().filter(i -> i.getId() == 1).findFirst().orElse(null);
        assertNotNull(item);
        inventory.remove(item);
        assertEquals(2, inventory.size());
    }

    @Test
    @DisplayName("존재하지 않는 id 삭제 시도")
    void deleteItem_notFound() {
        Item item = inventory.stream().filter(i -> i.getId() == 999).findFirst().orElse(null);
        assertNull(item);
    }

    // ── Rq 파싱 ───────────────────────────────────────────

    @Test
    @DisplayName("Rq 명령어 파싱")
    void rq_parseCmd() {
        Rq rq = new Rq("삭제?id=1");
        assertEquals("삭제", rq.getCmd());
    }

    @Test
    @DisplayName("Rq 파라미터 파싱")
    void rq_parseParams() {
        Rq rq = new Rq("수정?id=1&name=콜라&qty=20");
        assertEquals("1", rq.getParams("id", "0"));
        assertEquals("콜라", rq.getParams("name", ""));
        assertEquals("20", rq.getParams("qty", "0"));
    }

    @Test
    @DisplayName("Rq 파라미터 없을 때 기본값 반환")
    void rq_defaultValue() {
        Rq rq = new Rq("삭제");
        assertEquals(0, rq.getIntParam("id", "0"));
    }
}
