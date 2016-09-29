package com.aiitec.technicalreserve.zxing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aiitec.openapi.view.annatation.ContentView;
import com.aiitec.technicalreserve.BaseActivity;
import com.aiitec.technicalreserve.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

import java.util.Hashtable;

/**
 * @author Anthony
 *         createTime 2016/8/8.
 * @version 1.0
 */
public class ZxingActivity extends BaseActivity implements View.OnClickListener{
    private static final int CHOOSE_PIC = 0;
    private static final int PHOTO_PIC = 1;

    private EditText contentEditText = null;
    private ImageView qrcodeImageView = null;
    private String  imgPath = null;

    @ContentView(R.layout.activity_zxing)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        setTitle("二维码扫描");
        contentEditText = (EditText) findViewById(R.id.editText1);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        qrcodeImageView = (ImageView) findViewById(R.id.img1);
    }

    //解析二维码图片,返回结果封装在Result对象中
    private com.google.zxing.Result  parseQRcodeBitmap(String bitmapPath){
        //解析转换类型UTF-8
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //获取到待解析的图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        //如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)
        //并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
        options.inJustDecodeBounds = true;
        //此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath,options);
        //我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素
        /**
         options.outHeight = 400;
         options.outWidth = 400;
         options.inJustDecodeBounds = false;
         bitmap = BitmapFactory.decodeFile(bitmapPath, options);
         */
        //以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性
        options.inSampleSize = options.outHeight / 400;
        if(options.inSampleSize <= 0){
            options.inSampleSize = 1; //防止其值小于或等于0
        }
        /**
         * 辅助节约内存设置
         *
         * options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888
         * options.inPurgeable = true;
         * options.inInputShareable = true;
         */
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        //新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
        //将图片转换成二进制图片
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //初始化解析对象
        QRCodeReader reader = new QRCodeReader();
        //开始解析
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgPath = null;
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case CHOOSE_PIC:
                    String[] proj = new String[]{MediaStore.Images.Media.DATA};
                    Cursor cursor = ZxingActivity.this.getContentResolver().query(data.getData(), proj, null, null, null);

                    if(cursor.moveToFirst()){
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        System.out.println(columnIndex);
                        //获取到用户选择的二维码图片的绝对路径
                        imgPath = cursor.getString(columnIndex);
                    }
                    cursor.close();

                    //获取解析结果
                    Result ret = parseQRcodeBitmap(imgPath);
                    if(ret != null){
                        Toast.makeText(ZxingActivity.this,"解析结果：" + ret.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ZxingActivity.this,"解析结果：" + ret, Toast.LENGTH_LONG).show();
                    }
                    break;
                case PHOTO_PIC:
                    String result = data.getExtras().getString(CaptureActivity.KEY_RESULT);
                    Toast.makeText(ZxingActivity.this,"解析结果：" + result, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    }

    @SuppressLint("InlinedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                //获取界面输入的内容
                String content = contentEditText.getText().toString();
                //判断内容是否为空
                if (null == content || "".equals(content)) {
                    Toast.makeText(ZxingActivity.this, "请输入要写入二维码的内容....",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    //生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
                    Bitmap qrcodeBitmap = EncodingHandler.createQRCode(content, 400);
                    Bitmap logo = BitmapFactory.decodeResource(getResources(),R.drawable.qqkongjian_share);//logo图片
                    qrcodeImageView.setImageBitmap(addLogo(qrcodeBitmap,logo));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.button2:
                //跳转到图片选择界面去选择一张二维码图片
                Intent intent1 = new Intent();
//			if(android.os.Build.VERSION.SDK_INT < 19){
//				intent1.setAction(Intent.ACTION_GET_CONTENT);
//			}else{
//				intent1.setAction(Intent.ACTION_OPEN_DOCUMENT);
//			}
                intent1.setAction(Intent.ACTION_PICK);

                intent1.setType("image/*");

                Intent intent2 =  Intent.createChooser(intent1, "选择二维码图片");
                startActivityForResult(intent2, CHOOSE_PIC);
                break;
            case R.id.button3:
                //跳转到拍照界面扫描二维码
                Intent intent3 = new Intent(ZxingActivity.this, CaptureActivity.class);
                startActivityForResult(intent3, PHOTO_PIC);
                break;

            default:
                break;
        }

    }

    /**
     * 在二维码中间添加Logo图案
     * @param src 生成的二维码
     * @param logo 要加在中间的logo
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

}
