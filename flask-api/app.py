from flask import Flask, request, jsonify
import torch
from transformers import BertTokenizer, BertForSequenceClassification
from flask_cors import CORS

app = Flask(__name__)

# CORS 설정 (모든 도메인에서의 요청을 허용)
CORS(app)

# BERT 모델 로드 (fine-tuned 모델)
model = BertForSequenceClassification.from_pretrained('bert-base-multilingual-cased', num_labels=18)

# 모델 가중치 로드
model.load_state_dict(torch.load('model/model.pth', weights_only=True))

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
            with torch.no_grad():
                outputs = model(**inputs)
                logits = outputs.logits
                app.logger.info("Logits: %s", logits)
                prediction = torch.argmax(logits, dim=1).item()
                app.logger.info("Prediction: %d", prediction)

            label_map = {
                0: "곡류", 1: "조미식품", 2: "유제품류", 3: "채소류", 4: "가공식품류",
                5: "즉석식품류", 6: "기타식품류", 7: "과일류", 8: "육류 및 그 제품",
                9: "어패류 및 그 제품", 10: "빙과류", 11: "음료류", 12: "과자류·빵류 또는 떡류",
                13: "식용유지류", 14: "코코아가공품류 또는 초콜릿류", 15: "절임류 또는 조림류",
                16: "특수영양식품", 17: "특수의료용도식품"
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
