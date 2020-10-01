package com.example.proect.ui.home;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddImageActivty extends AppCompatActivity {
    ImageView imageView;
    Button choose, upload, download;
    Uri filepath;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_image_activity);

        imageView = findViewById(R.id.upload_image_view);
        upload = findViewById(R.id.upload_button);
        download = findViewById(R.id.download_button);
        choose = findViewById(R.id.select_button);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,0);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddImageActivty.this,
                        HomeFragment.class));

            }
        });

        storageReference = FirebaseStorage.getInstance().
                getReference("Stored images/");
        databaseReference = FirebaseDatabase.getInstance().
                getReference("Images");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            filepath = data.getData();
            Picasso.with(this)
                    .load(filepath)
                    .into(imageView);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadImage () {
        if (filepath != null) {
            StorageReference storageRef = storageReference.child(
                    System.currentTimeMillis()+ ":" +
                    getFileExtension(filepath));
            storageRef.putFile(filepath).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddImageActivty.this,"successful",
                            Toast.LENGTH_SHORT).show();
                    Task<Uri> result = taskSnapshot.getMetadata().
                            getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Upload upload = new Upload();
                            upload.setUrl(String.valueOf(uri));
                            String key = databaseReference.push().getKey();
                            databaseReference.child(key).setValue(upload);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddImageActivty.this,"",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String getFileExtension (Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}