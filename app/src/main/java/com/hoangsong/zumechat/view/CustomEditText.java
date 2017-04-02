package com.hoangsong.zumechat.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.hoangsong.zumechat.R;


//
// An EditText that lets you use actions ("Done", "Go", etc.) on multi-line edits.
public class CustomEditText extends EditText
{
	private Typeface typeFace;
	
    public CustomEditText(Context context)
    {
        super(context);
        typeFace= Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font));
        setTypeface(typeFace);

    }

    public CustomEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        typeFace= Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font));
        setTypeface(typeFace);


    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        typeFace= Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font));
        setTypeface(typeFace);


    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }
}