package dk.ba.bastampcard.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import dk.ba.bastampcard.R;

/**
 * Created by Benedicte on 10-11-2014.
 */
public class ShareActivity extends Activity {
    Uri uriImage;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
    }

    public void clickOnImage(View view){
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                uriImage = data.getData();
                Log.d(uriImage.toString(), "image selected");
                showImage();
            }
        }
    }

    public void onClickShare(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uriImage);
        startActivity(Intent.createChooser(intent, "Image has been sent..."));
    }

    public void showImage(){
        ImageView imageView = (ImageView) findViewById(R.id.viewImage);
        imageView.setImageURI(uriImage); //The path to the image
    }
}
