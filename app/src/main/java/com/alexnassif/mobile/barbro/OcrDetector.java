/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alexnassif.mobile.barbro;


import android.util.SparseArray;

import com.alexnassif.mobile.barbro.utilities.GraphicOverlay;
import com.google.android.gms.vision.text.TextBlock;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetector {

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private SparseArray<TextBlock> results;

    OcrDetector(GraphicOverlay<OcrGraphic> ocrGraphicOverlay, SparseArray<TextBlock> r) {
        mGraphicOverlay = ocrGraphicOverlay;
        results = r;
        detect();
    }

    public void detect(){
        for (int i = 0; i < results.size(); ++i) {
            TextBlock item = results.valueAt(i);
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
   /* @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }
    }

    *//**
     * Frees the resources associated with this detection processor.
     *//*
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }*/
}
