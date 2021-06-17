package com.monsalud.classblaster.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.monsalud.classblaster.Database.CBRepository;
import com.monsalud.classblaster.Entity.TermEntity;
import com.monsalud.classblaster.R;

import java.time.LocalDate;
import java.util.List;

public class TermListActivity extends AppCompatActivity
implements AddTermFragment.OnCancelAddTermListener, AddTermFragment.OnAddTermListener {

    private TermRecyclerAdapter mTermRecyclerAdapter;
    private Fragment mFragment;
    private CBRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CourseActivity.id_currentTerm = -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        initializeDisplayContents();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mTermRecyclerAdapter.notifyDataSetChanged();
    }
    private void initializeDisplayContents() {
        mRepository = new CBRepository(getApplication());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<TermEntity> terms = mRepository.getAllTerms();

        final RecyclerView recyclerTerms = (RecyclerView) findViewById(R.id.list_terms);
        final LinearLayoutManager termsLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTerms.setLayoutManager(termsLayoutManager);

        mTermRecyclerAdapter = new TermRecyclerAdapter(this, terms);
        recyclerTerms.setAdapter(mTermRecyclerAdapter);
    }

    //Opens the AddTermFragment
    public void addTerm(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        mFragment = AddTermFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.termlistcontainer, mFragment)
                .addToBackStack(null).commit();
        }

    //Closes the Fragment and navigates back to the hosting Activity
    @Override
    public void onCancel() {
        mFragment.getActivity().onBackPressed();
    }

    //Adds the Term from the AddTermFragment using the currently displayed information
    @Override
    public void onAddTerm() {
        //Determine whether term is active or not active
        boolean isActive;
        if (AddTermFragment.bIsActive.isChecked()) isActive = true;
        else isActive = false;

        //Get the id number of the last term in the arraylist and increment it
        List<TermEntity> allTerms = mRepository.getAllTerms();
        int id = allTerms.get(allTerms.size() - 1).getTerm_id();
        id++;

        //Create a Term object using the user-entered information and insert it into the Database
        TermEntity term = new TermEntity(
                id,
                String.valueOf(AddTermFragment.etTermName.getText()),
                LocalDate.parse(AddTermFragment.dpTermStartDate.getText()),
                LocalDate.parse(AddTermFragment.dpTermEndDate.getText()),
                isActive);

        mRepository.insertTerm(term);

        //Close the soft keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) view = new View(this);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        /*Notify the Recycler Adapter that the data set has changed then re-initialize the list
         with the new new Term included and go back to the TermListActivity screen
         */
        mTermRecyclerAdapter.notifyDataSetChanged();
        initializeDisplayContents();
        mFragment.getActivity().onBackPressed();
    }
}
