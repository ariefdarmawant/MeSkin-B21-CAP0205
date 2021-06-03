from flask import Flask, request, jsonify
import cv2
from model import load_model
import urllib
import numpy as np
from keras.preprocessing import image
from PIL import Image

app = Flask(__name__)

model = load_model()
labels = ['lentigo NOS','lichenoid keratosis', 'melanoma', 'nevus', 'seborrheic keratosis']

@app.route('/', methods=['GET', 'POST'])
def handle_request():
    return "Server is running and connnected."

@app.route('/predict', methods=['POST'])
def predictor():
    def image_from_url(url):
        request = urllib.request.urlopen(url)
        image = np.asarray(bytearray(request.read()), dtype="uint8")
        image = cv2.imdecode(image, cv2.IMREAD_COLOR)
        return image

    content = request.json
    img = Image.fromarray(image_from_url(content["image_path"])).resize((128,128))
    x=np.expand_dims(img, axis=0)
    images = np.vstack([x])
    classes = model.predict(images)
    return jsonify(status='SUCCESS',predict=labels[np.argmax(classes[0])])

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=False)
