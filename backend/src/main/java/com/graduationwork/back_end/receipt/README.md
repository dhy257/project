# 영수증 OCR 및 상품 관리 백엔드

## 개요

이 프로젝트는 영수증 이미지를 업로드하여 네이버 OCR API를 통해 텍스트를 추출하고,  
추출된 텍스트에서 상품명을 파싱한 후 예측 모델을 통해 카테고리를 산출하는 백엔드 시스템입니다.  
사용자는 예측 결과를 확인 및 수정한 후 최종 상품 정보를 데이터베이스에 저장할 수 있으며,  
저장된 상품을 상태별로 조회 및 업데이트할 수 있습니다.

## 시스템 흐름도

```bash
사용자
  │
  ▼
[영수증 이미지 업로드 + userId]
  │
  ▼
ReceiptController (/receipt/upload)
  │
  ▼
ReceiptService
  │ ──> 파일 저장
  │ ──> OCR 처리 (ReceiptOcr) ──> 네이버 OCR API
  │ ──> JSON 파싱 (상품명 추출)
  │ ──> 예측 API 호출 (/predict) ──> 예측 결과 획득
  │
  ▼
PredictedItemDto 리스트 반환
  │
  ▼
사용자 확인/수정
  │
  ▼
ItemController (/item/save)
  │
  ▼
ItemRepository → DB 저장
  │
  ▼
ItemDto 리스트 반환 (저장 결과 확인)
  │
  ▼
추가: 상태별 아이템 조회 및 업데이트 API
```


1. **영수증 업로드**
  - **Method:** POST
  - **URL:** `/receipt/upload`
  - **설명:**  
    영수증 이미지와 userId를 받아 파일 저장, 네이버 OCR API를 통한 텍스트 추출, 상품명 파싱, 예측 API 호출 후  
    PredictedItemDto 리스트 반환

2. **최종 상품 정보 저장**
  - **Method:** POST
  - **URL:** `/item/save`
  - **설명:**  
    사용자가 확인/수정한 상품 정보를 받아 해당 영수증에 연결된 Item들을 DB에 저장하고,  
    저장된 ItemDto 리스트를 반환

3. **보관중 상품 조회**
  - **Method:** GET
  - **URL:** `/item/storage`
  - **설명:**  
    DB에 저장된 상품 중 상태가 "보관중"인 항목들을 조회하여 반환

4. **아이템 상태 업데이트**
  - **Method:** PUT
  - **URL:** `/item/status`
  - **설명:**  
    특정 아이템의 상태를 "보관중", "사용", "버림" 등으로 업데이트

5. **직접 물품 추가 (영수증 없이)**
  - **Method:** POST
  - **URL:** `/item/direct-add`
  - **설명:**  
    사용자가 영수증 없이 직접 물품을 추가하면, 새 Receipt를 생성하고 auto-assigned receipt_id와 연결하여  
    Item들을 DB에 저장한 후, ItemDto 리스트를 반환


## API 엔드포인트

### 1. 영수증 업로드 API
- **HTTP Method:** POST  
- **URL:** `http://localhost:8080/receipt/upload`  
- **설명:**  
  - 사용자가 영수증 이미지 파일과 사용자 ID를 전송하면, 서버가 파일을 저장한 후 네이버 OCR API를 통해 이미지에서 텍스트를 추출합니다.
  - 추출된 OCR 텍스트에서 상품명을 파싱하고, 각 상품명에 대해 예측 API(`/predict`)를 호출하여 카테고리를 산출합니다.
  - 예측 결과는 `PredictedItemDto` 리스트 형태로 클라이언트에 반환됩니다.
- **요청 예시 (multipart/form-data):**
  - `file`: 영수증 이미지 파일  
  - `userId`: 사용자 ID (예: 11)
  - **응답 예시 (JSON):**
    ```json
    [
        {
            "receiptId": 11,
            "productName": "아이스크림",
            "category": "즉석식품류"
        },
        {
            "receiptId": 11,
            "productName": "황도825G",
            "category": "즉석식품류"
        }
        // 나머지 상품들...
    ]

### 2. 최종 상품 정보 저장 API
- **HTTP Method:** POST
- **URL:** `http://localhost:8080/item/save`
  - **설명:**
    - 사용자가 예측 결과를 확인 및 수정한 후 최종 상품 정보를 전송하면, 해당 영수증에 연결된 상품(Item)들을 데이터베이스에 저장합니다.
    - 저장된 상품 정보는 `ItemDto` 리스트 형태로 클라이언트에 반환됩니다.
  - **요청 예시 (JSON):**
  - 
    ```json
      {
          "receiptId": 11,
          "items": [
              {
                  "productName": "아이스크림",
                  "category": "즉석식품류",
                  "expirationDate": "2025-12-31"
              },
              {
                  "productName": "황도825G",
                  "category": "즉석식품류",
                  "expirationDate": ""
              }
              // 추가 상품들...
          ]
      }
    
  - **응답 예시 (JSON):**
    
     ```json
        [
      {
          "name": "아이스크림",
          "category": "즉석식품류",
          "expirationDate": "2025-12-31",
          "status": "보관중"
      },
      {
          "name": "황도825G",
          "category": "즉석식품류",
          "expirationDate": null,
          "status": "보관중"
      }
      // 저장된 나머지 상품들...
    ]

### 3. 보관중인 상품 조회 API
- **HTTP Method:** GET
- **URL:** `http://localhost:8080/item/storage`
- **설명:**
  - 데이터베이스에 저장된 상품들 중 상태가 "보관중"인 상품만 조회합니다.
  - 별도의 요청 파라미터는 필요하지 않습니다.

- **응답 예시 (JSON):**
  ```json
  [
      {
          "name": "아이스크림",
          "category": "즉석식품류",
          "expirationDate": "2025-12-31",
          "status": "보관중"
      },
      {
          "name": "황도825G",
          "category": "즉석식품류",
          "expirationDate": null,
          "status": "보관중"
      }
      // "보관중" 상태의 나머지 상품들...
  ]



### 4. 아이템 상태 업데이트 API

- **HTTP Method:** PUT
- **URL:** `http://localhost:8080/item/status`
- **설명:**
  - 사용자가 특정 상품의 상태를 "보관중", "사용", "버림" 등으로 변경할 수 있도록 업데이트합니다.

- **요청 예시 (JSON):**
  ```json
  {
      "itemId": 5,
      "status": "사용"
  }

- **위 예시는 아이템 ID가 5인 상품의 상태를 "사용"으로 변경하는 요청입니다.**

- 응답 예시 (JSON):
  ```json
  {
      "name": "아이스크림",
      "category": "즉석식품류",
      "expirationDate": "2025-12-31",
      "status": "사용"
  }
사용 방법
- 아이템 ID 확인: 상태를 변경하려는 아이템의 ID를 알아야 합니다.
- 요청 전송: /item/status 엔드포인트에 PUT 요청을 보내고, itemId와 status 값을 JSON 형태로 전달합니다.
- 결과 확인: 응답으로 변경된 상태가 반영된 상품 정보가 반환됩니다.

### 5. **직접 물품 추가 (영수증 없이)**
  - **Method:** POST
  - **URL:** `http://localhost:8080/item/direct-add`
  - **설명:**  
    사용자가 영수증 없이 직접 물품을 추가하면, 새 Receipt를 생성하고 auto-assigned receipt_id와 연결하여  
    Item들을 DB에 저장한 후, ItemDto 리스트를 반환

- **요청 예시 (JSON):**
```json
{
  "userId": 11,
  "items": [
    {
    "productName": "아이스크림",
    "category": "즉석식품류",
    "expirationDate": "2025-12-31"
    },
    {
    "productName": "황도825G",
    "category": "즉석식품류",
    "expirationDate": ""
    }
]
