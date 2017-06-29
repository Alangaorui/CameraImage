package com.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***
 *在此处写下来调用系统的相册的并且带有相机的功能的方法，
 * 由于业务需求，写来一个demo来看这个能否实现
 * 带多张上传的功能
 * 刚开始是用的base６４上传的照片但有许多的问题
 * 服务器的字节过长，
 * 实在不行就是想到用输入流和输出流的方法来坐的并且方便高效
 */

public class MainActivity extends Activity {
    private ArrayList<String> selectedImagesPaths = new ArrayList<String>();
    private static final int REQUEST_CODE_GET_PHOTOS = 1000;
//    private static final String requestURL = "";
    private static final String requestURL = "";//服务端的地址
    String pathlist;
    File file;
    private Button button;
    private  Map<String, String> params;
    private  Map<String, File> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_demo);
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},1);
        button = (Button) findViewById(R.id.btn_comint);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (params != null && files != null) {
                        String request = UploadUtil.post(requestURL, params, files);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    /**
     * 照片选择
     *
     * @param view
     */
    public void choosePhoto(View view) {
        Intent i = new Intent(this, PhotoSelectorActivity.class);
        i.putStringArrayListExtra("selectedPaths", selectedImagesPaths);//若传入已选中的路径则在选择页面会呈现选中状态
        startActivityForResult(i, REQUEST_CODE_GET_PHOTOS);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GET_PHOTOS:
                if (resultCode == RESULT_OK) {
                    selectedImagesPaths = data.getStringArrayListExtra("selectPaths");
//                    Log.e("sss", "sss" + selectedImagesPaths.get(0).toString());
                    if (selectedImagesPaths.size() >0) {
                        for (int i = 0; i < selectedImagesPaths.size(); i++) {
                            pathlist = selectedImagesPaths.get(i).toString();
                            Log.e("string", "string:" + pathlist);
                            file = new File(pathlist);
                            Log.e("file", "file:" + file);
                            params = new HashMap<String, String>();
                            params.put("userId", "1");
                            params.put("name","alan");

                            files = new HashMap<String, File>();
                            files.put("uploadfile", file);

                        }
                    }

                    Log.e("file", "file1:" + file);
//				File file = new File(selectedImagesPaths.get(0).toString());
//				final Map<String, String> params = new HashMap<String, String>();
//				params.put("send_userId", String.valueOf("1"));
//
//				final Map<String, File> files = new HashMap<String, File>();
//				files.put("uploadfile", file);
//				try {
//					final String request = UploadUtil.post(requestURL, params, files);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
                }
                break;
        }
    }

}