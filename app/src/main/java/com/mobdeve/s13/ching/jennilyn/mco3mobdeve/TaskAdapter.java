package com.mobdeve.s13.ching.jennilyn.mco3mobdeve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final List<Task> taskList;
    private final DatabaseHelper dbHelper;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvTaskTitle.setText(task.getTitle());
        holder.tvTaskDate.setText(task.getDate());
        holder.tvTaskTime.setText(task.getTime());

        if ("Not yet started".equals(task.getStatus())) {
            holder.rbNotStarted.setChecked(true);
        } else if ("In progress".equals(task.getStatus())) {
            holder.rbInProgress.setChecked(true);
        } else if ("Done".equals(task.getStatus())) {
            holder.rbDone.setChecked(true);
        }

        holder.rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            String status = "Not yet started";
            if (checkedId == R.id.rbInProgress) {
                status = "In progress";
            } else if (checkedId == R.id.rbDone) {
                status = "Done";
            }
            task.setStatus(status);
            dbHelper.updateTaskStatus(task.getId(), status);
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskDate, tvTaskTime;
        RadioGroup rgStatus;
        RadioButton rbNotStarted, rbInProgress, rbDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDate = itemView.findViewById(R.id.tvTaskDate);
            tvTaskTime = itemView.findViewById(R.id.tvTaskTime);
            rgStatus = itemView.findViewById(R.id.rgStatus);
            rbNotStarted = itemView.findViewById(R.id.rbNotStarted);
            rbInProgress = itemView.findViewById(R.id.rbInProgress);
            rbDone = itemView.findViewById(R.id.rbDone);
        }
    }
}
