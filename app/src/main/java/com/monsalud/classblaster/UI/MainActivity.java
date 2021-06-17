package com.monsalud.classblaster.UI;
        ;

import androidx.appcompat.app.AppCompatActivity;
        import androidx.room.RoomDatabase;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;

        import com.monsalud.classblaster.DAO.AssessmentDAO;
        import com.monsalud.classblaster.DAO.CourseDAO;
        import com.monsalud.classblaster.DAO.TermDAO;
        import com.monsalud.classblaster.Database.CBRepository;
        import com.monsalud.classblaster.Database.ClassBlasterDatabase;
        import com.monsalud.classblaster.Entity.TermEntity;
        import com.monsalud.classblaster.R;

        import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Navigates to the TermListActivity
    public void goToTermList(View view) {
        Intent intent = new Intent(MainActivity.this, TermListActivity.class);
        startActivity(intent);
    }
}