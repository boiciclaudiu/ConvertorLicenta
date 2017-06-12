package com.exemple.converter.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Celo on 11.01.2017.
 */

public class PhotoUtils {

    public static String compareHist(Bitmap bitmapTaken, Bitmap ron){
        Mat histTaken = calcHist(bitmapTaken);
        Mat histRon = calcHist(ron);

        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");

        double res;

        res = Imgproc.compareHist(histTaken, histRon, 1);
        sb.append("1. Correlation (H)" + df.format(res) + "\n");

        res = Imgproc.compareHist(histTaken, histRon, 2);
        sb.append("2. Chi-square (L)" + df.format(res) + "\n");

        res = Imgproc.compareHist(histTaken, histRon, 3);
        sb.append("3. Intersection (H)" + df.format(res) + "\n");

        res = Imgproc.compareHist(histTaken, histRon, 4);
        sb.append("4. Bhattacharyya (L)" + df.format(res) + "\n");


        return sb.toString();
    }

    private static Mat calcHist(Bitmap bitmap){
        Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1);
        Mat hsv = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1);
        Mat hist = new Mat();

        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2GRAY);

        List <Mat> mats = new ArrayList<>();
        mats.add(hsv);

        MatOfInt channels = new MatOfInt(0);
        MatOfInt histSize = new MatOfInt(10);
        MatOfFloat ranges = new MatOfFloat(0, 255);

        Imgproc.calcHist(mats, channels, new Mat(), hist, histSize, ranges);
        Core.normalize(hist, hist, 0 , 1, Core.NORM_MINMAX, -1, new Mat());

        return hist;
    }

}
