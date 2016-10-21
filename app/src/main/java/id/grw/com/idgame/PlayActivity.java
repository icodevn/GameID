package id.grw.com.idgame;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import id.grw.com.helper.DBHelper;
import id.grw.com.model.Level;
import id.grw.com.model.Question;

public class PlayActivity extends AppCompatActivity {

    private DBHelper myDB;

    private static final Integer MEDIUM_QUESTION =  5;

    public static final  Integer MAX_QUESTION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //Hide Action Bar
        getSupportActionBar().hide();
        myDB = new DBHelper(this);

        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        RadioButton radioValue1 = (RadioButton)findViewById(R.id.radioVal1);
        RadioButton radioValue2 = (RadioButton)findViewById(R.id.radioVal2);
        RadioButton radioValue3 = (RadioButton)findViewById(R.id.radioVal3);
        RadioButton radioValue4 = (RadioButton)findViewById(R.id.radioVal4);
        ImageView questionImg = (ImageView)findViewById(R.id.questionImage);
        Button btnNext = (Button) findViewById(R.id.btnNext);
        // Get Intent
        Intent intent = getIntent();
        String level = intent.getStringExtra("level");
        int standartPoint = getPointByLevel(level);
        int count = intent.getIntExtra("beginPlay",1);
        if(count <= 1 ){
            String idStr = convertfromList(myDB,level,MEDIUM_QUESTION);
            String[] arr = idStr.split(",");
            Question question = myDB.getQuestionById(Integer.parseInt(arr[count-1]));
            txtTitle.setText(question.getTitle());
            radioValue1.setText(question.getAnswer1());
            radioValue2.setText(question.getAnswer2());
            radioValue3.setText(question.getAnswer3());
            radioValue4.setText(question.getAnswer4());
            //set question image
            Drawable drawable = getDrawable(question.getImage());
            questionImg.setImageDrawable(drawable);
        }else if(count > 1 && count <= MEDIUM_QUESTION){
            intent.getStringExtra("idQuestion");
        }else{
            
        }

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    private Drawable getDrawable(String picName){
        String name ="";
        if(picName.contains("jpeg")){
            name = picName.substring(0,picName.length()-5);
        }else{
            name = picName.substring(0,picName.length()-4);
        }
        Resources res = getResources();
        int resId = res.getIdentifier(name,"drawable",getPackageName());
        Drawable drawable = res.getDrawable(resId);
        return drawable;
    }

    private Integer getPointByLevel(String level){
        int point = 0;
        switch (level){
            case Level.EASY: point = 10;
                break;
            case Level.MEDIUM : point = 15;
                break;
            case Level.HARD : point = 20;
                break;
        }
        return point;
    }

    private String convertfromList(DBHelper myDB,String level,int maxQuestion){
        String idList = "";
        List<Question> listQuestions = myDB.selectQuestionsByLevel(level);
        Collections.shuffle(listQuestions);
        int count = 0;
        for(Question question : listQuestions){
            if(count < maxQuestion) {
                idList += question.getId() + ",";
            }
            count ++;
        }
        idList.substring(0,idList.length()-1);
        return idList;
    }
}
