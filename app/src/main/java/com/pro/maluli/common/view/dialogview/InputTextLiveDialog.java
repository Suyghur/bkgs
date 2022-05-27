package com.pro.maluli.common.view.dialogview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.pro.maluli.R;


/**
 * @author ganhuanhui
 * 时间：2019/12/11 0011
 * 描述：https://blog.csdn.net/qq_32518491/article/details/85000421
 */
public class InputTextLiveDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private EditText messageTextView;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;
    private int maxNumber = 200;
    private OnTextSendListener mOnTextSendListener;

    public InputTextLiveDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        init();
        setLayout();
    }

    /**
     * 设置输入提示文字
     */
    public void setHint(String text) {
        messageTextView.setHint(text);
    }

    private void init() {
        setContentView(R.layout.dialog_input_text_msg_live);
        messageTextView = (EditText) findViewById(R.id.et_input_message);
        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        messageTextView.requestFocus();
        messageTextView.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView显示剩余字数
                selectionStart = messageTextView.getSelectionStart();
                selectionEnd = messageTextView.getSelectionEnd();
                if (wordNum.length() > 200) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    messageTextView.setText(s);
                    messageTextView.setSelection(tempSelection);//设置光标在最后
                }

            }
        });

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        ImageView iv_confirm = findViewById(R.id.iv_confirm);
        iv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (msg.length() > maxNumber) {
                    Toast.makeText(mContext, "超过最大字数限制" + maxNumber, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.isEmpty(msg)) {
                    mOnTextSendListener.onTextSend(msg);
                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    InputTextLiveDialog.this.dismiss();
                } else {
                    Toast.makeText(mContext, "请输入文字", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().length() > maxNumber) {
                            Toast.makeText(mContext, "超过最大字数限制", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if (messageTextView.getText().length() > 0) {
                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                            InputTextLiveDialog.this.dismiss();
                        } else {
                            Toast.makeText(mContext, "请输入文字", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        InputTextLiveDialog.this.dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                //获取当前界面可视部分
                InputTextLiveDialog.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = InputTextLiveDialog.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
                    InputTextLiveDialog.this.dismiss();
                }
                mLastDiff = heightDifference;
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    InputTextLiveDialog.this.dismiss();
                }
                return false;
            }
        });
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
        if (mOnTextSendListener != null) mOnTextSendListener.dismiss();

    }

    @Override
    public void show() {
        super.show();
    }

    public interface OnTextSendListener {

        void onTextSend(String msg);

        void dismiss();
    }
}
