package com.p.suraj.photos1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase database;
    private DatabaseReference myRef, myRef1;
    //  private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;
    private StorageReference mStorageRef;
    File actualImage;
    Uri uri;
    ArrayList list = new ArrayList();
    private Target loadtarget;
    RecyclerView recyclerView;
    Recycler_adapter recycler_adapter;
    RecyclerView.LayoutManager layoutManager;

    TextView textView;
    ArrayList<Image> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);


        textView = (TextView) findViewById(R.id.homeTxt1);
       /* YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(1)
                .playOn(textView);*/





    }


    public void onclick(View view) {
        try {
            AddDeptDialog nothingDialog = new AddDeptDialog();
            nothingDialog.show(getSupportFragmentManager(), "nothing dialog");
        }
        catch (Exception e){

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        myRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);


        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            myRef.child("Image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String ,?> keys = preferences.getAll();
                    int Count1 = 0;
                    for(Map.Entry<String,?> entry : keys.entrySet())
                    {
                        Bitmap mImageUri =decodeBase64(preferences.getString(entry.getKey(), null)) ;
                        Image v = new Image(mImageUri,entry.getKey());
                        if(dataSnapshot.hasChild(entry.getKey())){
                            if(!list.contains(entry.getKey())){
                                list.add(entry.getKey());
                                arrayList.add(v);
                            }
                        }
                        else{
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove(entry.getKey());
                            editor.commit();
                        }

                        //  Toast.makeText(MainActivity.this, "working : "+entry.getValue(), Toast.LENGTH_SHORT).show();

                    }



                    for(final DataSnapshot dsp:dataSnapshot.getChildren()){


                        String ohk = preferences.getString(dsp.getKey(), null);


                        if (ohk == null)  {

                            if (loadtarget == null) loadtarget = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                    byte[] b = new byte[0];

                                    Bitmap realImage = bitmap;
                                    if(realImage!=null) {

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        b = baos.toByteArray();
                                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);




                                        if(!list.contains(dsp.getKey())){
                                            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(MainActivity2.this);
                                            SharedPreferences.Editor edit = shre.edit();
                                            edit.putString(dsp.getKey(), encodedImage);
                                            edit.commit();
                                            Bitmap mImageUri =decodeBase64(preferences.getString(dsp.getKey(), null)) ;

                                            Image v = new Image(mImageUri,dsp.getKey());

                                            Toast.makeText(MainActivity2.this, "working3", Toast.LENGTH_SHORT).show();

                                            list.add(dsp.getKey());
                                            arrayList.add(v);

                                            try {
                                                recycler_adapter.notifyDataSetChanged();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }


                            };
                            Picasso.with(MainActivity2.this).load(dsp.child("image").getValue().toString()).into(loadtarget);




                        }
                        else{

                            Bitmap mImageUri =decodeBase64(preferences.getString(dsp.getKey(), null)) ;

                            Image v = new Image(mImageUri,dsp.getKey());

                            if(!list.contains(dsp.getKey())){
                                Toast.makeText(MainActivity2.this, "working2", Toast.LENGTH_SHORT).show();
                                list.add(dsp.getKey());
                                arrayList.add(v);


                            }
                        }



                    }

                    recyclerView = (RecyclerView )findViewById(R.id.recycleview);
                    layoutManager =new LinearLayoutManager(MainActivity2.this);
                    // recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    recyclerView.setHasFixedSize(true);

                    Recycler_adapter.context = MainActivity2.this;
                    recycler_adapter = new Recycler_adapter(arrayList);
                    recyclerView.setAdapter(recycler_adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {



            Map<String ,?> keys = preferences.getAll();
            int Count1 = 0;
            for(Map.Entry<String,?> entry : keys.entrySet())
            {
                Bitmap mImageUri =decodeBase64(preferences.getString(entry.getKey(), null)) ;
                Image v = new Image(mImageUri,entry.getKey());

                {
                    if(!list.contains(entry.getKey())){
                        list.add(entry.getKey());
                        arrayList.add(v);
                       // Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            Toast.makeText(this, "Internet Connection Is Required", Toast.LENGTH_LONG).show();
            recyclerView = (RecyclerView )findViewById(R.id.recycleview);
            layoutManager =new LinearLayoutManager(MainActivity2.this);
            // recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);

            recyclerView.setHasFixedSize(true);
            Recycler_adapter.context = MainActivity2.this;

            recycler_adapter = new Recycler_adapter(arrayList);

            recyclerView.setAdapter(recycler_adapter);
        }



    }
    @Override
    protected void onResume() {
        super.onResume();
      //  Toast.makeText(this, ""+arrayList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  Toast.makeText(this, ""+arrayList.size(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
