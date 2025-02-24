import requests
from flask import Flask, jsonify
import pandas as pd
from sklearn.neighbors import NearestNeighbors

app = Flask(__name__)

SPRING_API_BASE_URL = "http://localhost:8080/api/recommend"

# KNN 학습 함수
def train_knn(df):
    if df.empty:
        print("🚨 [Flask] 학습할 데이터가 없습니다! 모델을 생성할 수 없습니다.")
        return None, None

    print(f"✅ [Flask] {len(df)}개의 데이터로 KNN 모델을 학습합니다.")
    df_matrix = df.pivot_table(index="user_id", columns="ingredient_name", aggfunc="size", fill_value=0)
    model = NearestNeighbors(metric='cosine', algorithm='brute')
    model.fit(df_matrix)

    print("✅ [Flask] KNN 모델 학습이 완료되었습니다.")
    return model, df_matrix

# 추천 생성 함수
def get_recommendations(user_id, model, df_matrix):
    if model is None:
        print(f"🚨 [Flask] KNN 모델이 학습되지 않았습니다. 추천을 생성할 수 없습니다.")
        return []

    if user_id not in df_matrix.index:
        print(f"🚨 [Flask] 사용자 {user_id}의 데이터가 없습니다. 추천을 생성할 수 없습니다.")
        return []

    print(f"🔍 [Flask] 사용자 {user_id}의 추천을 생성 중입니다.")

    user_data = df_matrix.loc[[user_id]]
    n_neighbors = min(5, len(df_matrix))
    distances, indices = model.kneighbors(user_data, n_neighbors=n_neighbors)

    similar_users = df_matrix.iloc[indices.flatten()]
    weights = 1 - distances.flatten()
    weighted_recommendations = (similar_users.T * weights).T.sum(axis=0) / weights.sum()

    user_ingredients = set(df_matrix.loc[user_id].index[df_matrix.loc[user_id] > 0])
    recommended_items = weighted_recommendations.drop(index=user_ingredients, errors='ignore')

    recommendations = recommended_items.nlargest(5).index.tolist()
    print(f"✅ [Flask] 사용자 {user_id}에 대한 추천 결과: {recommendations}")
    return recommendations

# 사용한 식재료 추천
@app.route("/recommend/used/<int:user_id>", methods=["GET"])
def recommend_used(user_id):
    print(f"🔍 [Flask] 사용자 {user_id}의 사용한 식재료 추천 요청을 받았습니다.")

    spring_response = requests.get(f"{SPRING_API_BASE_URL}/used/all")

    if spring_response.status_code != 200:
        print("🚨 [Flask] Spring 서버에서 사용한 식재료 데이터를 가져오는 데 실패했습니다.")
        return jsonify({"error": "Spring 서버에서 데이터를 가져오는 데 실패했습니다."}), 500

    data = spring_response.json()
    df_used = pd.DataFrame(data)
    print(f"✅ [Flask] Spring 서버에서 {len(df_used)}개의 사용한 식재료 데이터를 받았습니다.")

    model_used, df_user_ingredient = train_knn(df_used)
    recommendations = get_recommendations(user_id, model_used, df_user_ingredient)

    print(f"🚀 [Flask] 추천 결과를 Spring 서버로 전송합니다: {recommendations}")
    save_response = requests.post(f"{SPRING_API_BASE_URL}/save", json={
        "user_id": user_id,
        "recommendation_type": "used",
        "recommended_ingredients": recommendations
    })

    if save_response.status_code == 200:
        print(f"✅ [Flask] Spring 서버에 추천 결과가 성공적으로 저장되었습니다! (사용자 {user_id})")
        return jsonify({"user_id": user_id, "recommended_ingredients": recommendations})
    else:
        print(f"🚨 [Flask] Spring 서버에 추천 결과 저장 실패! (사용자 {user_id})")
        return jsonify({"error": "Spring 서버에 추천 결과 저장 실패"}), 500

# 버린 식재료 추천
@app.route("/recommend/wasted/<int:user_id>", methods=["GET"])
def recommend_wasted(user_id):
    print(f"🔍 [Flask] 사용자 {user_id}의 버린 식재료 추천 요청을 받았습니다.")

    spring_response = requests.get(f"{SPRING_API_BASE_URL}/wasted/all")

    if spring_response.status_code != 200:
        print("🚨 [Flask] Spring 서버에서 버린 식재료 데이터를 가져오는 데 실패했습니다.")
        return jsonify({"error": "Spring 서버에서 데이터를 가져오는 데 실패했습니다."}), 500

    data = spring_response.json()
    df_wasted = pd.DataFrame(data)
    print(f"✅ [Flask] Spring 서버에서 {len(df_wasted)}개의 버린 식재료 데이터를 받았습니다.")

    model_wasted, df_user_waste = train_knn(df_wasted)
    recommendations = get_recommendations(user_id, model_wasted, df_user_waste)

    print(f"🚀 [Flask] 추천 결과를 Spring 서버로 전송합니다: {recommendations}")
    save_response = requests.post(f"{SPRING_API_BASE_URL}/save", json={
        "user_id": user_id,
        "recommendation_type": "wasted",
        "recommended_ingredients": recommendations
    })

    if save_response.status_code == 200:
        print(f"✅ [Flask] Spring 서버에 추천 결과가 성공적으로 저장되었습니다! (사용자 {user_id})")
        return jsonify({"user_id": user_id, "recommended_wasted_ingredients": recommendations})
    else:
        print(f"🚨 [Flask] Spring 서버에 추천 결과 저장 실패! (사용자 {user_id})")
        return jsonify({"error": "Spring 서버에 추천 결과 저장 실패"}), 500

if __name__ == "__main__":
    app.run(host="localhost", port=8000, debug=True)
