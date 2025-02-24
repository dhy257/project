from flask import Flask, request, jsonify
import torch
from transformers import BertTokenizer, BertForSequenceClassification
from flask_cors import CORS

app = Flask(__name__)

# CORS 설정 (모든 도메인에서의 요청을 허용)
CORS(app)

# BERT 모델 로드 (fine-tuned 모델)
model = BertForSequenceClassification.from_pretrained('bert-base-multilingual-cased', num_labels=9)

# 🔥 모델 가중치 로드 (CPU에서 실행하도록 수정)
model.load_state_dict(torch.load('model/model.pth', map_location=torch.device('cpu')))

# 모델을 평가 모드로 전환
model.eval()

# 토크나이저 로드
tokenizer = BertTokenizer.from_pretrained('bert-base-multilingual-cased')

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        if data is None:
            app.logger.error("No JSON data received")
            return jsonify({'error': 'No JSON data received'}), 400

        texts = data.get("text", [])
        if not isinstance(texts, list):
            texts = [texts]  # 단일 텍스트일 경우 리스트로 변환

        app.logger.info(f"Received data: {data}")
        app.logger.info(f"Received texts: {texts}")

        results = {}

        for text in texts:
            inputs = tokenizer(text, return_tensors='pt', padding=True, truncation=True, max_length=64)

            # 🔥 CPU에서 실행하도록 변경
            with torch.no_grad():
                outputs = model(**inputs)
                logits = outputs.logits
                app.logger.info("Logits: %s", logits)
                prediction = torch.argmax(logits, dim=1).item()
                app.logger.info("Prediction: %d", prediction)

            # 🔹 새로운 라벨 맵 적용
            label_map = {
                0: "가공식품",
                1: "간식",
                2: "신선식품",
                3: "어패류",
                4: "유제품",
                5: "육류",
                6: "음료",
                7: "조미식품",
                8: "즉석식품"
            }

            category_name = label_map.get(prediction, "알 수 없는 카테고리")
            app.logger.info("Category Name: %s", category_name)

            results[text] = category_name

        return jsonify({'predictions': results})

    except Exception as e:
        app.logger.error(f"Error: {str(e)}")
        return jsonify({'error': 'Internal Server Error'}), 500


if __name__ == '__main__':
    app.run(debug=True)
