package com.wxq.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 从底部弹出的菜单
 * Created by wuxiaoquan on 14-1-8.
 */
public class DialogMenu extends Dialog implements View.OnClickListener{

    //可以为空
    private View forView;
    private Context mContext;
    private DialogMenuListener listener;
    private DialogMenuListener defaultListener = new DialogMenuListener() {
        @Override
        public void onCreate(View parent) {

        }

        @Override
        public void onItemClick(DialogMenu dm, View v) {
            Toast.makeText(mContext,((TextView)v).getText() + ":请设置菜单动作",Toast.LENGTH_LONG).show();
        }
    };
    private int [] ids;
    private String [] titles;
    private int [] titleRes;

    public DialogMenu(Context context,View forView, DialogMenuListener listener,int [] ids,String [] titles) {
        super(context,R.style.DialogNoBackground);
        this.mContext = context;
        this.forView = forView;
        this.listener = listener;
        if (this.listener == null)
        {
            this.listener = defaultListener;
        }
        this.ids = ids;
        this.titles = titles;
    }

    public DialogMenu(Context context,View forView, DialogMenuListener listener,int [] ids,int [] titleRes) {
        super(context,R.style.DialogNoBackground);
        this.mContext = context;
        this.forView = forView;
        this.listener = listener;
        if (this.listener == null)
        {
            this.listener = defaultListener;
        }
        this.ids = ids;
        this.titleRes = titleRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.mail_list_context_menu);
        setCanceledOnTouchOutside(true);
        ViewGroup parent = (ViewGroup) findViewById(R.id.context_menu_bottom);
        LayoutInflater inflater = getLayoutInflater();

        int titleSize = 0;
        if (titleRes != null)
        {
            titleSize = titleRes.length;
        }
        else
        {
            titleSize = titles.length;
        }
        int minSize = Math.min(ids.length,titleSize);
        for (int i = 0; i < minSize; i++) {
            TextView view = (TextView) inflater.inflate(R.layout.mail_list_context_menu_item,parent,false);

            view.setId(ids[i]);
            if (titles != null)
            {
                view.setText(titles[i]);
            }
            else
            {
                view.setText(titleRes[i]);
            }

            view.setOnClickListener(this);
            parent.addView(view);
        }
        //设置取消按钮
        TextView cancelView = (TextView) inflater.inflate(R.layout.mail_list_context_menu_item,parent,false);
        cancelView.setText("取消");
        cancelView.setId(R.id.cancel);
        cancelView.setTextColor(Color.parseColor("#7b7b7b"));
        //去除最后的横线
        cancelView.setBackgroundResource(R.drawable.dialogmenu_textview_cancel_selector);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        parent.addView(cancelView);

        //最后设置View的机会
        listener.onCreate(parent);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = MATCH_PARENT;
        lp.height = WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {

        listener.onItemClick(this, v);
    }

    public interface DialogMenuListener
    {
        /**
         * 在构建菜单时刻的回调
         * @param parent 菜单的主体view
         */
        void onCreate(View parent);

        /**
         * 处理菜单项点击事件
         * @param dm 菜单
         * @param v 被点击的菜单项view
         */
        void onItemClick(DialogMenu dm, View v);
    }
}
