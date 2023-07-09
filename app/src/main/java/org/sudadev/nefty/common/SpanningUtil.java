package org.sudadev.nefty.common;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class SpanningUtil {
    public static void putAsteriskString(TextView textView) {
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(textView.getText().toString());
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
    }

    public static void setClickableString(String wholeValue, TextView textView,
                                          final String[] clickableValue,
                                          ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(wholeValue);

        for (int i = 0; i < clickableValue.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = clickableValue[i];

            int startIndexOfLink = wholeValue.indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setHighlightColor(Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

}
