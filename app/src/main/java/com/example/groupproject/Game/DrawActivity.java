package com.example.groupproject.Game;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;
import com.example.groupproject.User.LoginPage;
import com.example.groupproject.User.MainActivity;
import com.example.groupproject.Util.ToastUtil;

import java.io.IOException;
import java.io.OutputStream;


public class DrawActivity extends AppCompatActivity {

    private Button backBtn, clearBtn, exitBtn;
    private SeekBar brushSizeSeekBar;
    private RadioGroup colorRadioGroup;
    private DrawingView drawingView;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        initializeView();
        setupBrushSizeSeekBar();
        setupColorRadioGroup();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将图画保存到相册
                // Drawing saved to Gallery
                saveImageToGallery();
                // 跳转到登录页面
                // Navigate to the login page
                Intent intent = new Intent(DrawActivity.this, LoginPage.class);
                startActivity(intent);
            }
        });

    }

    private void initializeView() {
        drawingView = findViewById(R.id.drawingView);

        brushSizeSeekBar = findViewById(R.id.brushSizeSeekBar);
        colorRadioGroup = findViewById(R.id.colorRadioGroup);

        backBtn = findViewById(R.id.back_btn);
        clearBtn = findViewById(R.id.clear_btn);
        exitBtn = findViewById(R.id.exit_btn);

    }
    private void setupBrushSizeSeekBar() {
        brushSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 设置画笔大小
                drawingView.setBrushSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 不需要实现
            }
        });

    }

    private void setupColorRadioGroup() {
        colorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.redRadioButton:
                        color = Color.RED;
                        break;
                    case R.id.greenRadioButton:
                        color = Color.GREEN;
                        break;
                    case R.id.blueRadioButton:
                        color = Color.BLUE;
                        break;
                    default:
                        color = Color.BLACK;
                        break;
                }
                drawingView.setBrushColor(color);
            }
        });
    }

    /**
     * save drawing to the Gallery
     */

    private void saveImageToGallery() {
        // 创建位图并将绘图视图内容绘制到位图上
        // Create a bitmap and draw the contents of the drawing view onto it
        Bitmap bitmap = Bitmap.createBitmap(drawingView.getWidth(), drawingView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 绘制白色背景
        // Draw a white background
        canvas.drawColor(Color.WHITE);
        drawingView.draw(canvas);

        // 保存位图到设备的图库中
        // Save the bitmap to the device's gallery
        String fileName = "drawing_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri item = getContentResolver().insert(collection, values);

        if (item != null) {
            try {
                OutputStream outputStream = getContentResolver().openOutputStream(item);
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    // Display a toast message indicating successful save
                    ToastUtil.showMsg(DrawActivity.this, "Drawing saved to Gallery");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
