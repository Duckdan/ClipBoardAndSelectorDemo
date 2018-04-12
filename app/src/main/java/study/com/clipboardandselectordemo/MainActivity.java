package study.com.clipboardandselectordemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private String TAG = "actionMode";
    private CheckBox cb;
    private TextView tvContent;
    private EditText etContent;
    private TextView tvChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        tvContent = (TextView) findViewById(R.id.tv_content);
        etContent = (EditText) findViewById(R.id.et_content);
        cb = (CheckBox) findViewById(R.id.cb);
        tvChange = (TextView) findViewById(R.id.tv_change);

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改CheckBox是否被激活的状态
                cb.setEnabled(cb.isEnabled() ? false : true);
                tvChange.setText("改变状态:"+cb.isEnabled());
            }
        });


        clipAndCopy();
    }

    /**
     * 剪切板的处理
     */
    private void clipAndCopy() {
        tvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String content = tvContent.getText().toString().trim();
                //当SDK版本大于11时
                if (Build.VERSION.SDK_INT > 11) {
                    //获取到剪切板管理类
                    ClipboardManager clipboard = (ClipboardManager) context
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    //将要赋值的内容添加至剪切数据对象中，此处指的是普通文本
                    //第一个参数就是一个标签的作用，基本没有什么用处，随便也可以
                    //第二个参数是真正要添加至剪切版的内容
                    ClipData clip = ClipData.newPlainText("Simple text", content);
                    //将剪切数据对象交给剪切板管理者，当用户触发可以复制的View的时候，
                    // 一旦用户点击复制，剪切板管理器将会从剪切板中拿出数据。
                    /**
                     * 注意：剪切板管理类中只会有最近一条保存至剪切板管理类的数据
                     */
                    clipboard.setPrimaryClip(clip);

                } else {
                    android.text.ClipboardManager clip = (android.text.ClipboardManager) context
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    //将需要复制的内容交给剪切板管理类
                    clip.setText(content);

                }
                Toast.makeText(MainActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //设置EditText不能够复制
        etContent.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                Log.e(TAG, "onCreateActionMode");
                Toast.makeText(MainActivity.this, "不可全选", Toast.LENGTH_SHORT).show();
                //返回fasle会让当前的EditText不支持全选，也不可以复制
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                Log.e(TAG, "onPrepareActionMode");
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Log.e(TAG, "onActionItemClicked");
                return false;
            }
        });
    }
}
