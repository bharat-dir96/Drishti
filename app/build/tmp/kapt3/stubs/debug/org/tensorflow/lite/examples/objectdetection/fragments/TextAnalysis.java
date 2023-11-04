package org.tensorflow.lite.examples.objectdetection.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0017R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2 = {"Lorg/tensorflow/lite/examples/objectdetection/fragments/TextAnalysis;", "Landroidx/camera/core/ImageAnalysis$Analyzer;", "()V", "recognizer", "Lcom/google/mlkit/vision/text/TextRecognizer;", "getRecognizer", "()Lcom/google/mlkit/vision/text/TextRecognizer;", "recognizerOptions", "Lcom/google/mlkit/vision/text/latin/TextRecognizerOptions;", "getRecognizerOptions", "()Lcom/google/mlkit/vision/text/latin/TextRecognizerOptions;", "analyze", "", "imageProxy", "Landroidx/camera/core/ImageProxy;", "app_debug"})
public final class TextAnalysis implements androidx.camera.core.ImageAnalysis.Analyzer {
    @org.jetbrains.annotations.NotNull
    private final com.google.mlkit.vision.text.latin.TextRecognizerOptions recognizerOptions = null;
    @org.jetbrains.annotations.NotNull
    private final com.google.mlkit.vision.text.TextRecognizer recognizer = null;
    
    public TextAnalysis() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.google.mlkit.vision.text.latin.TextRecognizerOptions getRecognizerOptions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.google.mlkit.vision.text.TextRecognizer getRecognizer() {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"})
    @java.lang.Override
    public void analyze(@org.jetbrains.annotations.NotNull
    androidx.camera.core.ImageProxy imageProxy) {
    }
}