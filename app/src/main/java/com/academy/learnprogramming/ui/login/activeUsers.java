package com.academy.learnprogramming.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.academy.learnprogramming.R;
import com.academy.learnprogramming.data.model.UserNaNo;
import com.academy.learnprogramming.data.model.WordAdapter;

import java.util.ArrayList;

public class activeUsers extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_users);

        final ArrayList<UserNaNo> toDisplayList =  (ArrayList<UserNaNo>)getIntent().getSerializableExtra("FILES_TO_SEND");
        if(toDisplayList.size()<1){finish();}

//        for(UserNaNo t : toDisplayList){
//            Toast.makeText(getApplicationContext()," "+t.getNumber()+" "+t.getUserName(), Toast.LENGTH_SHORT).show();
//        }

        WordAdapter adapter = new WordAdapter(this, toDisplayList, R.color.category_numbers);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String number = toDisplayList.get(position).getNumber();
                String url = "https://api.whatsapp.com/send?phone=+91"+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void userOnclick(View view) {

    }

}
