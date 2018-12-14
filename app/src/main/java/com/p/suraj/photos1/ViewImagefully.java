package com.p.suraj.photos1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ViewImagefully extends AppCompatActivity {

    ViewPager viewPager;

    public static  ArrayList<Image> list;

    public static  int position1;
    private RelativeLayout rootLayout;


    private int swatchNumber;
    private Palette.Swatch vibrantSwatch;
    private Palette.Swatch lightVibrantSwatch;
    private Palette.Swatch darkVibrantSwatch;
    private Palette.Swatch mutedSwatch;
    private Palette.Swatch lightMutedSwatch;
    private Palette.Swatch darkMutedSwatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewimagefully);

        rootLayout = findViewById(R.id.root_layout);



    }

    @Override
    protected void onStart() {
        super.onStart();

        viewPager = (ViewPager) findViewById(R.id.viewpager2);
        ViewPagerAdapter2 viewPagerAdapter = new ViewPagerAdapter2(this);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position1);




    }

    public class ViewPagerAdapter2 extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;


        public ViewPagerAdapter2(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View view = layoutInflater.inflate(R.layout.photoview, null);
            //nextSwatch(view);

            /*Important to get some animation

            if we uncommented the above nextSwatch(View) this we show some animation which
             exctract the colors from the image and assign the those to the background.

             */


            PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);


            try {

               //  Picasso.with(ViewImagefully.this).load(list.get(position).getImage().toString()).into(photoView);

                   photoView.setImageBitmap(list.get(position).getImage());

                Palette.from(list.get(position).getImage()).maximumColorCount(32).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        vibrantSwatch = palette.getVibrantSwatch();
                        lightVibrantSwatch = palette.getLightVibrantSwatch();
                        darkVibrantSwatch = palette.getDarkVibrantSwatch();
                         mutedSwatch = palette.getMutedSwatch();
                        lightMutedSwatch = palette.getLightMutedSwatch();
                        darkMutedSwatch = palette.getDarkMutedSwatch();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


            //photoView.setImageResource(images[position]);

            final int finalPosition = position;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if(finalPosition == 0){
                        Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();

                    } else if(finalPosition == 1){
                        Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;



        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);


        }

    }



    public void nextSwatch(View v) {
        Palette.Swatch currentSwatch = null;

        switch (swatchNumber) {
            case 0:
                currentSwatch = vibrantSwatch;
                //textViewTitle.setText("Vibrant");
                break;
            case 1:
                currentSwatch = lightVibrantSwatch;
               // textViewTitle.setText("Light Vibrant");
                break;
            case 2:
                currentSwatch = darkVibrantSwatch;
               // textViewTitle.setText("Dark Vibrant");
                break;
            case 3:
                currentSwatch = mutedSwatch;
               // textViewTitle.setText("Muted");
                break;
            case 4:
                currentSwatch = lightMutedSwatch;
               // textViewTitle.setText("Light Muted");
                break;
            case 5:
                currentSwatch = darkMutedSwatch;
              //  textViewTitle.setText("Dark Muted");
                break;
        }

        if (currentSwatch != null) {
            rootLayout.setBackgroundColor(currentSwatch.getRgb());

        } else {
            rootLayout.setBackgroundColor(Color.WHITE);

        }

        if (swatchNumber < 5) {
            swatchNumber++;
        } else {
            swatchNumber = 0;
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}
