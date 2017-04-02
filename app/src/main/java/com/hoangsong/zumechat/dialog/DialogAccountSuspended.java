package com.hoangsong.zumechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.hoangsong.zumechat.R;


/**
 * Created by Tang on 05/05/2016.
 */
public class DialogAccountSuspended extends Dialog {

    public DialogAccountSuspended(final Context context,String message,String title) {
        super(context);
        //getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_account_suspended);
        setCancelable(true);
        //this.getWindow().setBackgroundDrawableResource(R.color.transparent_black);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        TextView textSuspendedTitle = (TextView) findViewById(R.id.text_suspended_title);
        final TextView textSuspendedMessageLine1 = (TextView) findViewById(R.id.text_suspended_messgae_line1);
        TextView textSuspendedMessageLine2 = (TextView) findViewById(R.id.text_suspended_messgae_line2);

      /*  String line1 = context.getString(R.string.lbl_account_suspended_message_line1).replace("{0}", Constants.EMAIL_ACCOUNT_SUSPENDED).replace("{1}", Constants.WEEK_ACCOUNT_SUSPENDED+"");
        //textSuspendedMessageLine1.setText(context.getString(R.string.lbl_account_suspended_message_line1).replace("{0}", Constants.EMAIL_ACCOUNT_SUSPENDED).replace("{1}", Constants.WEEK_ACCOUNT_SUSPENDED+""));

        final String keywordTOU = Constants.EMAIL_ACCOUNT_SUSPENDED;

        ColorClickableSpan spanTOU = new ColorClickableSpan(Color.YELLOW, context.getResources().getColor(R.color.red_01), context.getResources().getColor(R.color.transparent), Utils.getFontAvenirRoman(context)) {
            @Override
            public void onClick(View textView) {
                textSuspendedMessageLine1.setHighlightColor(Color.TRANSPARENT);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + keywordTOU));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.contact_title));
                context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.title_send_mail)));
            }
        };

        SpannableString contentTOU = new SpannableString(line1);
        contentTOU.setSpan(spanTOU, line1.indexOf(keywordTOU), line1.indexOf(keywordTOU) + keywordTOU.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textSuspendedMessageLine1.setText(contentTOU);
        textSuspendedMessageLine1.setHighlightColor(Color.TRANSPARENT);

        textSuspendedMessageLine1.setMovementMethod(LinkMovementMethod.getInstance());*/
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //sendBookingEvent(Constants.PUSH_FINISH_APP);
    }
    /*private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }*/
}
