package com.example.a33626.endhomework2.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;
import com.example.a33626.endhomework2.task.EditHeadPortraitTask;
import com.example.a33626.endhomework2.toast.MyToast;
import com.example.a33626.endhomework2.utils.ImageUtil;
import com.example.a33626.endhomework2.utils.MyThreadPool;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class HeadPortraitActivity extends AppCompatActivity implements AllActivityBasicTask {

    private User user;
    private ImageView headPortraitImageViewPhoto;
    private ImageView headPortraitImageViewReturn;
    private Button headPortraitButtonSelect;
    private Uri cropUri;
    private String path = Environment.getExternalStorageDirectory() +
            File.separator + Environment.DIRECTORY_DCIM + File.separator; //照片存取路径
    private String imagePath;
    private EditHeadPortraitTask editHeadPortraitTask;
    private MyThreadPool myThreadPool;
    private Handler headPortraitHandler;
    private MyToast myToast;
    private SqlUtil sqlUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_portrait);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        this.user = (User)intent.getSerializableExtra(Constants.USER_INFO);
        this.headPortraitImageViewPhoto = findViewById(R.id.headportrait_imageview_photo);
        this.headPortraitImageViewReturn = findViewById(R.id.headportrait_imageview_return);
        this.headPortraitButtonSelect = findViewById(R.id.headportrait_button_select);
        this.myThreadPool = MyThreadPool.getInstance();
        if (this.headPortraitImageViewPhoto == null){
            this.headPortraitImageViewPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.emoji));
        }
        else{
            this.headPortraitImageViewPhoto.setImageBitmap(ImageUtil.blobToBitMap(this.user.getBlobHeadPortrait()));
        }
        this.sqlUtil = new SqlUtil(this);
        this.headPortraitHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.EDIT_USER_HEAD_PORTRAIT:
                        user.setBlobHeadPortrait(ImageUtil.bitMapToBlob(BitmapFactory.decodeFile(imagePath)));
                        int resNum = sqlUtil.updateData(user);
                        headPortraitImageViewPhoto.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                        showMyToast("Upload is success",Constants.CORRECT);
                        break;
                }
            }
        };
    }

    @Override
    public void startAllEvent() {
        this.headPortraitButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, Constants.SETTING_ALBUM_REQUEST_CODE);
            }
        });

        this.headPortraitImageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == Constants.SETTING_ALBUM_REQUEST_CODE){
                if (data != null){
                    imagePath = path + "hp_img_" + UUID.randomUUID() + ".jpg";
                    File photoOutputFile = new File(imagePath);
                    //根据路径创建图片URI
                    if (Build.VERSION.SDK_INT >= 24) {
                        //因为Google更新 所以得用FileProvider创建URi
                        cropUri = FileProvider.getUriForFile(HeadPortraitActivity.this,
                                HeadPortraitActivity.this.getPackageName() + ".fileprovider",
                                photoOutputFile);
                    } else {
                        //从文件中创建uri
                        cropUri = Uri.fromFile(photoOutputFile);
                    }

                    //先请求权限
                    List resInfoList = getPackageManager().queryIntentActivities(data, PackageManager.MATCH_DEFAULT_ONLY);
                    Iterator resInfoIterator = resInfoList.iterator();
                    while (resInfoIterator.hasNext()) {
                        ResolveInfo resolveInfo = (ResolveInfo) resInfoIterator.next();
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, cropUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }

                    //创建好uri就可以开始剪裁了
                    startCrop(data.getData());
                }
            }
            else if (requestCode == Constants.SETTING_CORP_REQUEST_CODE){
                if (cropUri != null) {
                    editHeadPortraitTask = new EditHeadPortraitTask(headPortraitHandler);
                    user.setStringHeadPortrait(ImageUtil.imageToBase64(ImageUtil.bitMapToBlob(BitmapFactory.decodeFile(imagePath))));
                    editHeadPortraitTask.init(user);
                    myThreadPool.execute(editHeadPortraitTask);
                }
            }
        }
    }


    /**
     * 剪裁
     * @param uri
     */
    public void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置可剪裁
        intent.putExtra("corp", true);
        //设置宽和高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("scale",true);//缩放
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent,Constants.SETTING_CORP_REQUEST_CODE);
    }

    //调用自定义toast
    private void showMyToast(String content,String type) {
        if (this.myToast != null) {
            this.myToast.hide();
        }
        this.myToast = new MyToast(this,
                (ViewGroup) findViewById(R.id.toast_linearlayout_parent), type);
        this.myToast.show(content,1000);
    }
}
