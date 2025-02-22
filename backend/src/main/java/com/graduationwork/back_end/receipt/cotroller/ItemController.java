package com.graduationwork.back_end.receipt.cotroller;

import com.graduationwork.back_end.UserRepository;
import com.graduationwork.back_end.receipt.dto.*;
import com.graduationwork.back_end.receipt.model.Item;
import com.graduationwork.back_end.receipt.model.ItemStatus;
import com.graduationwork.back_end.receipt.model.Receipt;
import com.graduationwork.back_end.receipt.repository.ItemRepository;
import com.graduationwork.back_end.receipt.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.graduationwork.back_end.User;
import com.graduationwork.back_end.UserRepository;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자가 최종 수정한 상품 정보를 받아 해당 Receipt에 연결된 Item DB 저장
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveItems(@RequestBody FinalizeItemsRequest request) {
        Receipt receipt = receiptRepository.findById(request.getReceiptId())
                .orElseThrow(() -> new RuntimeException("해당 영수증을 찾을 수 없습니다."));

        List<Item> savedItems = new ArrayList<>();
        request.getItems().forEach(dto -> {
            Item item = new Item();
            item.setReceipt(receipt);
            item.setName(dto.getProductName());
            item.setCategory(dto.getCategory());
            item.setStatus(ItemStatus.보관중);
            if (dto.getExpirationDate() != null && !dto.getExpirationDate().isEmpty()) {
                item.setExpirationDate(LocalDate.parse(dto.getExpirationDate()));
            }
            savedItems.add(itemRepository.save(item));
        });

        // 🔥 DTO 변환 후 반환
        List<ItemDto> responseDto = savedItems.stream()
                .map(ItemDto::new)
                .toList();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/storage")
    public ResponseEntity<List<ItemDto>> getStorageItems(@RequestParam("userId") Long userId) {
        List<Item> items = itemRepository.findByStatusAndReceiptUserUserId(ItemStatus.보관중, userId);
        List<ItemDto> responseDto = items.stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDto);
    }
    @PutMapping("/status")
    public ResponseEntity<?> updateItemStatus(@RequestBody UpdateItemStatusRequest request) {
        Optional<Item> optionalItem = itemRepository.findById(request.getItemId());
        if (!optionalItem.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }
        Item item = optionalItem.get();
        try {
            // 요청한 상태가 enum에 정의된 값인지 확인 (대소문자 주의)
            ItemStatus newStatus = ItemStatus.valueOf(request.getStatus());
            item.setStatus(newStatus);
            itemRepository.save(item);
            return ResponseEntity.ok(new ItemDto(item));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + request.getStatus());
        }
    }

    // 영수증 없이 직접 물품을 추가하는 API - POST /item/direct-add
    @PostMapping("/direct-add")
    public ResponseEntity<?> addItemsDirectly(@RequestBody ManualAddItemsRequest request) {
        // 1. 사용자 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        // 2. 수동 등록용 Receipt 생성 (영수증 이미지/텍스트는 없음)
        Receipt receipt = new Receipt();
        receipt.setUser(user);
        receipt.setReceiptImage(null);
        receipt.setReceiptText("Manual entry");
        Receipt savedReceipt = receiptRepository.save(receipt);

        // 3. 요청 받은 Item 정보를 기반으로 Item 생성 및 저장
        List<Item> savedItems = new ArrayList<>();
        for (ItemRequestDto dto : request.getItems()) {
            Item item = new Item();
            item.setReceipt(savedReceipt);
            item.setName(dto.getProductName());
            item.setCategory(dto.getCategory());
            item.setStatus(ItemStatus.보관중);
            if (dto.getExpirationDate() != null && !dto.getExpirationDate().isEmpty()) {
                item.setExpirationDate(LocalDate.parse(dto.getExpirationDate()));
            }
            savedItems.add(itemRepository.save(item));
        }

        // 4. 저장된 Item을 DTO로 변환하여 반환
        List<ItemDto> responseDto = savedItems.stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDto);
    }

}
