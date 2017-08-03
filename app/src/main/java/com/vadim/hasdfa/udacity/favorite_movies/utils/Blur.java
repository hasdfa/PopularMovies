package com.vadim.hasdfa.udacity.favorite_movies.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

public class Blur implements Transformation {
    protected static final int UP_LIMIT = 25;
    protected static final int LOW_LIMIT = 1;
    protected final Context context;
    protected final int blurRadius;


    public Blur(Context context, int radius) {
        this.context = context;

        if(radius<LOW_LIMIT){
            this.blurRadius = LOW_LIMIT;
        }else if(radius>UP_LIMIT){
            this.blurRadius = UP_LIMIT;
        }else
            this.blurRadius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source) {

        Bitmap blurredBitmap;
        blurredBitmap = source.copy(source.getConfig(), true);

        RenderScript renderScript = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(renderScript,
                source,
                Allocation.MipmapControl.MIPMAP_FULL,
                Allocation.USAGE_SCRIPT);



        Allocation output = Allocation.createTyped(renderScript, input.getType());

        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));

        script.setInput(input);
        script.setRadius(blurRadius);

        script.forEach(output);
        output.copyTo(blurredBitmap);

        source.recycle();
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blurred";
    }
}