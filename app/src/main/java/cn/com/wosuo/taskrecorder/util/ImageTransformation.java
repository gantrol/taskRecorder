package cn.com.wosuo.taskrecorder.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

public class ImageTransformation {

    public static Transformation getTransformation(final ImageView imageView) {
        return new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = imageView.getWidth();
                int targetHeight = imageView.getHeight();
                double targetRatio = (double) targetHeight / (double) targetWidth;
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                if (aspectRatio > targetRatio) {  //高则设宽为目标宽，宽则设高为目标高
                    targetHeight = (int) (targetWidth * aspectRatio);
                } else {
                    targetWidth = (int) (targetHeight / aspectRatio);
                }
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
    }
}