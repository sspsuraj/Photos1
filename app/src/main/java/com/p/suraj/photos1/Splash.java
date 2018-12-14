package com.p.suraj.photos1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Splash extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

       // Utils.darkenstatusbar(this, R.color.white);
      //  ImageView imageView = findViewById(R.id.imageView);

        TextView textView = (TextView) findViewById(R.id.text) ;
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        //textView.startAnimation(animation);


        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(1)
                .playOn(textView);


        Thread timer = new Thread(){



            @Override

            public void run() {



                try {

                    sleep(2000);


                    Intent intent = new Intent(getApplicationContext(),MainActivity2.class);

                    startActivity(intent);

                    finish();

                    super.run();

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }





            }

        };



        timer.start();

    }
}
