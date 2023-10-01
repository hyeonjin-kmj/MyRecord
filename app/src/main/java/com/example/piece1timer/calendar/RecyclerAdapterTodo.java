package com.example.piece1timer.calendar;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piece1timer.R;
import com.example.piece1timer.diary.MyDiary;

public class RecyclerAdapterTodo extends RecyclerView.Adapter<RecyclerAdapterTodo.ViewHolder> {
    Context context;
    int checkedPosition = -1;

    ChangeTodoStateDialog dialog;

    FragmentManager fManager;

    //선택 listener 정의

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    OnItemClickListener mListener = null;

    RecyclerAdapterTodo(Context context ,FragmentManager fManager) {
        this.context = context;
        this.fManager = fManager;
    }

    String editted_todo;

    @NonNull
    @Override
    public RecyclerAdapterTodo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.recycler_item_todo, parent, false);

        return new RecyclerAdapterTodo.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerItemTodo todo = MyDiary.tList.get(position);
        holder.todo.setText(todo.todo);
        holder.state.setImageDrawable(todo.state);
    }

    @Override
    public int getItemCount() {
        return MyDiary.tList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText todo;
        ImageButton state, btn_delete;

        ViewHolder(View itemView) {
            super(itemView);

            this.todo = itemView.findViewById(R.id.todo);
            this.state = itemView.findViewById(R.id.state);
            this.btn_delete = itemView.findViewById(R.id.btn_todo_delete);

            //edittext 이벤트 정리
            todo.setCursorVisible(false);

            todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EditText)v).setInputType(EditorInfo.TYPE_CLASS_TEXT);
                }
            });

            todo.setImeOptions(EditorInfo.IME_ACTION_DONE);
            todo.setOnEditorActionListener(new TextView.OnEditorActionListener() { //입력 완료 후 발생하는 이벤트
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    editted_todo = v.getText().toString();
                    v.setInputType(EditorInfo.TYPE_NULL);
                    editTodo(editted_todo);
                    return true;
                }
            });

            //상태 변경
            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new ChangeTodoStateDialog();
                    dialog.setDialogListener(new ChangeTodoStateDialog.CustomDialogListener() {
                        @Override
                        public void onCancel() {}
                        @Override
                        public void onStateChanged(int 바뀐진행상태) {
                            editState(바뀐진행상태);
                        }
                    });
                    dialog.show(fManager, dialog.TAG_EVENT_DIALOG);
                }
            });

            //삭제
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedPosition = getAbsoluteAdapterPosition();
                    if (checkedPosition != RecyclerView.NO_POSITION && mListener != null) mListener.onItemClick(v,checkedPosition);
                    deleteTodo();
                }
            });
        }

        //각 메서드 구현
        RecyclerItemTodo del_todo;
        String[] 현재todo들;
        String updated_todo = "";
        public void deleteTodo () {
            checkedPosition = getAbsoluteAdapterPosition();
            del_todo = MyDiary.tList.get(checkedPosition);

            if (!MyDiary.DBtodo.getString(del_todo.yyyyMMdd, " ").contains("|")){//지정된 날짜에 할 일이 하나면
                MyDiary.editorT.remove(del_todo.yyyyMMdd);
            }
            else {//여러 개면
                현재todo들 = MyDiary.DBtodo.getString(del_todo.yyyyMMdd, " ").split("\\|");
                for (int i=0; i< 현재todo들.length; i++){//선택한 일정 삭제
                    if(i!=checkedPosition){
                        updated_todo+=현재todo들[i]+"|";
                    }
                }
                MyDiary.editorT.putString(del_todo.yyyyMMdd, updated_todo.substring(0, updated_todo.length()-1));//마지막의 "|" 지우기
            }
            MyDiary.tList.remove(checkedPosition);
            notifyItemRemoved(checkedPosition);

            MyDiary.editorT.apply();

            checkedPosition = -1;
            del_todo = null;
            현재todo들 = null;
            updated_todo = "";
        }

        RecyclerItemTodo edit_todo;

        public void editTodo (String 바뀐내용) {
            checkedPosition = getAbsoluteAdapterPosition();
            edit_todo = MyDiary.tList.get(checkedPosition);//오늘 할 일 중 지금 거

            if (!MyDiary.DBtodo.getString(edit_todo.yyyyMMdd, " ").contains("|")){//지정된 날짜에 할 일이 하나면
                MyDiary.editorT.putString(edit_todo.yyyyMMdd, 바뀐내용+"*"+edit_todo.key_name);
            }
            else {//설정된 값이 여러 개면
                현재todo들 = MyDiary.DBtodo.getString(edit_todo.yyyyMMdd, " ").split("\\|");
                for (int i=0; i< 현재todo들.length; i++){
                    if(i!=checkedPosition)updated_todo+=현재todo들[i]+"|";
                    else updated_todo+= 바뀐내용+"*"+edit_todo.key_name+"|";
                }
                MyDiary.editorT.putString(edit_todo.yyyyMMdd, updated_todo.substring(0, updated_todo.length()-1));//마지막의 "|" 지우기
            }
            MyDiary.editorT.apply();

            notifyItemChanged(checkedPosition);
            checkedPosition = -1;
            del_todo = null;
            현재todo들 = null;
            updated_todo = "";
        }

        //이 메서드가 사용될 시점 = dialog에서 사용자가 새 상태를 선택하고(각 onclick) 창이 dismiss될 때. 지금 이 클래스 안에서 그 시점을 정해주고 싶음.
        public void editState (int 바뀐진행상태) {//state_name = R.drawable.done
            checkedPosition = getAbsoluteAdapterPosition();
            edit_todo = MyDiary.tList.get(checkedPosition);//오늘 할 일 중 지금 편집할 RecyclerItemtodo.
            edit_todo.setKey_name(바뀐진행상태);
            if (!MyDiary.DBtodo.getString(edit_todo.yyyyMMdd, " ").contains("|")){//지정된 날짜에 할 일이 하나면
                MyDiary.editorT.putString(edit_todo.yyyyMMdd, edit_todo.todo+"*"+edit_todo.key_name);
            }
            else {//설정된 값이 여러 개면
                현재todo들 = MyDiary.DBtodo.getString(edit_todo.yyyyMMdd, " ").split("\\|");
                for (int i=0; i< 현재todo들.length; i++){
                    if(i!=checkedPosition){
                        updated_todo+=현재todo들[i]+"|";
                    }
                    else {
                        updated_todo+= edit_todo.todo+"*"+edit_todo.key_name+"|";
                    }
                }
                MyDiary.editorT.putString(edit_todo.yyyyMMdd, updated_todo.substring(0, updated_todo.length()-1));//마지막의 "|" 지우기
            }
            MyDiary.editorT.apply();
            notifyItemChanged(checkedPosition);
            checkedPosition = -1;
            del_todo = null;
            현재todo들 = null;
            updated_todo = "";
        }
    }

}
