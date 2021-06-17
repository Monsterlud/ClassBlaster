package com.monsalud.classblaster.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.monsalud.classblaster.Entity.AssessmentEntity;
import com.monsalud.classblaster.Entity.NoteEntity;
import com.monsalud.classblaster.R;

import java.util.List;

public class  NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<NoteEntity> mNotes;
    private final LayoutInflater mLayoutInflater;

    public NoteRecyclerAdapter(Context context, List<NoteEntity> notes) {
        mContext = context;
        mNotes= notes;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteEntity note = mNotes.get(position);
        holder.mTextViewNoteName.setText(note.getNote_name());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
            return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextViewNoteName;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewNoteName = (TextView) itemView.findViewById(R.id.textview_notename);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoteEntity currentNote = mNotes.get(mCurrentPosition);
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_ID, currentNote.getNote_id());
                    intent.putExtra(NoteActivity.NOTE_NAME, currentNote.getNote_name());
                    intent.putExtra(NoteActivity.NOTE_FIELD, currentNote.getNote_field());
                    intent.putExtra(NoteActivity.NOTE_COURSE_ID, currentNote.getCourse_id_fk());

                    mContext.startActivity(intent);
                }
            });
        }
    }
}
