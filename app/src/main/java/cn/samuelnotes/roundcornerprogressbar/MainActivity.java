package cn.samuelnotes.roundcornerprogressbar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import cn.samuelnotes.roundcornerprogressbar.view.ProgressView;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private ProgressView progress_view_00;
    private ProgressView progress_view_01;
    private ProgressView progress_view_02;
    private ProgressView progress_view_03;
    private ProgressView progress_view_04;
    private ProgressView progress_view_05;
    private ProgressView progress_view_06;
    private EditText edit_num;
    private Button set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress_view_00 = (ProgressView) findViewById(R.id.progress_view_00);
        progress_view_01 = (ProgressView) findViewById(R.id.progress_view_01);
        progress_view_02 = (ProgressView) findViewById(R.id.progress_view_02);
        progress_view_03 = (ProgressView) findViewById(R.id.progress_view_03);
        progress_view_04 = (ProgressView) findViewById(R.id.progress_view_04);
        progress_view_05 = (ProgressView) findViewById(R.id.progress_view_05);
        progress_view_06 = (ProgressView) findViewById(R.id.progress_view_06);

        start = (Button) findViewById(R.id.start);


        final ValueAnimator animator = ObjectAnimator.ofInt(0, 100);
        animator.setDuration(1000 * 10);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress_view_00.setProgress((int) valueAnimator.getAnimatedValue(), false);
                progress_view_01.setProgress((int) valueAnimator.getAnimatedValue(), false);
                progress_view_02.setProgress((int) valueAnimator.getAnimatedValue(), false);
                progress_view_03.setProgress((int) valueAnimator.getAnimatedValue(), false);
                progress_view_04.setProgress((int) valueAnimator.getAnimatedValue(), false);
                progress_view_05.setProgress((int) valueAnimator.getAnimatedValue(), false);
                progress_view_06.setProgress((int) valueAnimator.getAnimatedValue(), false);
            }
        });

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animator.start();
            }
        });

        edit_num = (EditText) findViewById(R.id.edit_num);
        set = (Button) findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit_num.getText().toString();
                int num = Integer.parseInt(text);
                if (num > 100) {
                    num = 100;
                }
                progress_view_00.setProgress(num);
                progress_view_01.setProgress(num);
                progress_view_02.setProgress(num);
                progress_view_03.setProgress(num);
                progress_view_04.setProgress(num);
                progress_view_05.setProgress(num);
                progress_view_06.setProgress(num);
            }
        });


    }

}
