# -*- coding: utf-8 -*-
"""
Created on Thu Apr 11 03:13:10 2024

@author: metin
"""

import sys
from pydub import AudioSegment
import torch
from transformers import pipeline  # Используем pipeline из transformers

# Путь к модели на Hugging Face
MODEL_NAME = "UlutSoftLLC/whisper-small-kyrgyz"

# Загружаем модель и токенизатор используя pipeline
transcription_pipeline = pipeline(model=MODEL_NAME, task="automatic-speech-recognition")

def transcribe(audio_path):
    # Whisper работает напрямую с файлами, но для предобработки мы используем pydub для конвертации в нужный формат
    audio = AudioSegment.from_file(audio_path).set_channels(1).set_frame_rate(16000)
    # Экспортируем аудио во временный файл wav
    audio.export("temp.wav", format="wav")
    # Используем pipeline для транскрипции
    result = transcription_pipeline("temp.wav")
    print(result['text'])

if __name__ == "__main__":
    if len(sys.argv) > 1:
        audio_path = sys.argv[1]
        transcribe(audio_path)
    else:
        print("No audio file specified.")
