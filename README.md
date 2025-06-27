# Cancer Detection App (Android + Machine Learning)

A simple Android application to detect cancer from an image using a pre-trained TensorFlow Lite machine learning model.

## 📱 Features

- Select an image from the gallery
- Display the selected image as a preview
- Run inference using a TensorFlow Lite model provided by Dicoding
- Display the prediction result and confidence score

## 🧠 Technologies Used

- **Android (Kotlin)**
- **TensorFlow Lite (TFLite)**
- **Dicoding-provided ML model**
- Image selection via **Intent Gallery**
- Prediction result shown in a separate **ResultActivity**

## 🚀 How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/AgungKusumma/cancer-detection-app.git
   ```
   
2. Open the project in Android Studio.

3. Make sure to place the provided .tflite model inside the assets/ folder:
   model_cancer_classification.tflite

4. Run the project on an emulator or physical device with Android 7.0 (API 24) or higher.
