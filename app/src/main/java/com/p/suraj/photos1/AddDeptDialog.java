package com.p.suraj.photos1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import id.zelory.compressor.Compressor;


import static android.app.Activity.RESULT_OK;


public class AddDeptDialog extends AppCompatDialogFragment
{
    Button button;
    java.io.File actualImage;
    private StorageReference mStorageRef;
    Uri uri;
    public static TextView time ;
    ImageView imageView ;
    EditText projectname;
    DatabaseReference myRef;
    public static  String timeStamp;

    ProgressDialog progressDialog;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater= getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.editdatadialogue3,null);
        builder.setView(view);
        projectname = (EditText)view.findViewById(R.id.editText5);
        myRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        button = (Button) view.findViewById(R.id.button3);
        progressDialog = new ProgressDialog(getActivity());

        imageView = (ImageView) view.findViewById(R.id.imageView3);
         imageView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
             }
             catch (Exception e) {
            e.printStackTrace();
             }
         }
         });


    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            // progressDialog.set
            progressDialog.setCanceledOnTouchOutside(false);
            try {

                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {



                    if (uri != null) {
                        //  Toast.makeText(getActivity(), ""+uri, Toast.LENGTH_SHORT).show();
                        Long tsLong = System.currentTimeMillis();
                        timeStamp = tsLong.toString();

                        try {
                            try {
                                {

                                    try {
                                        java.io.File compressedImageFile = new Compressor(getActivity()).compressToFile(actualImage);
                                        Uri file = Uri.fromFile(compressedImageFile);
                                        StorageReference riversRef = mStorageRef.child("Photos").child(timeStamp);

                                        riversRef.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@android.support.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {
                                                task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        final String s = taskSnapshot.getDownloadUrl().toString();
                                                        myRef.child("Image").child(timeStamp).child("image").setValue(s);

                                                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                                                        startActivity(intent);
                                                        progressDialog.dismiss();
                                                        uri = null;
                                                    }
                                                });

                                            }
                                        });


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else{
                        Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Internet Connection is required", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });









        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            try {
                if(requestCode==1 && resultCode==RESULT_OK) {

                    try {
                        if(!data.equals(null)){
                            actualImage = Filee.from(getActivity(), data.getData());}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(!data.equals(null)){
                            uri = data.getData();}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    imageView.setImageURI(uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
