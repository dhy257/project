import torch

# 모델 로드
model = torch.load('flask-api/model/model.pth')
model.eval()  # 모델을 평가 모드로 전환

print("모델이 성공적으로 로드되었습니다!")
