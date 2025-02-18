# Food Category Classification Backend

이 프로젝트는 BERT 모델을 사용하여 영수증 항목을 분류하는 백엔드 애플리케이션입니다. 사용자가 입력한 영수증 데이터를 Flask API로 처리하여, Spring 백엔드가 이를 관리하고 사용자에게 결과를 반환합니다.

## 사용된 기술 스택

- **Backend Framework**: Spring Framework (Java)
- **API**: Flask (Python) - BERT 모델 예측
- **Model**: BERT (Pretrained BERT 모델을 사용하여 텍스트 분류)
- **Database**: MySQL (식품 카테고리 데이터 저장)
- **API Testing**: Postman

## 설치 방법

### 1. **Flask API 설치 및 실행**

1. Flask API 폴더로 이동합니다.

    ```bash
    cd flask-api
    ```

2. 가상 환경을 설정하고 활성화합니다.

    ```bash
    python -m venv venv
    source venv/bin/activate  # Mac/Linux
    venv\Scripts\activate     # Windows
    ```


3. 필요한 패키지를 설치합니다.

    ```bash
    pip install --upgrade pip setuptools
   pip install torch
   pip install flask
   pip install transformers
   pip install flask-cors


    ```

4. Flask 서버를 실행합니다.

    ```bash
    python app.py
    ```

   Flask API는 기본적으로 **http://localhost:5000**에서 실행됩니다.

### 2. **Spring Backend 설치 및 실행**

1. Spring Backend 폴더로 이동합니다.

    ```bash
    cd backend
    ```

2. Spring 애플리케이션을 실행합니다.

    ```bash
    ./gradlew bootRun
    ```

   Spring 서버는 기본적으로 **http://localhost:8080**에서 실행됩니다.

## API 테스트 (Postman 사용)

### 1. **Flask API 테스트**

1. **POST** 요청을 **http://localhost:5000/predict**에 보냅니다.
2. **Body** 탭에서 `raw`와 `JSON`을 선택하고 아래와 같은 데이터를 입력합니다:

    ```json
    {
      "items": ["milk", "bread", "cherry tomatoes"]
    }
    ```

3. **응답 예시**:

    ```json
    {
      "predictions": [0, 1, 2]
    }
    ```

### 2. **Spring API 테스트**

1. **POST** 요청을 **http://localhost:8080/api/classify-items**에 보냅니다.
   2. **Body** 탭에서 `raw`와 `JSON`을 선택하고 아래와 같은 데이터를 입력합니다:

       ```json
   [ 
      "고기",
      "배추",
      "당근"
   ]

    ```