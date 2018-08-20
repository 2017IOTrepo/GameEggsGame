package com.example.a41448.huawu.Face;

/**
 * @Description: 识别数据监听
 */
public interface IDataListener<T> {
    void onDetectorData(DetectorData<T> detectorData);
}
