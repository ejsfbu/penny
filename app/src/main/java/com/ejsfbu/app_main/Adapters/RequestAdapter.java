package com.ejsfbu.app_main.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ejsfbu.app_main.Models.Goal;
import com.ejsfbu.app_main.Models.Request;
import com.ejsfbu.app_main.Models.Reward;
import com.ejsfbu.app_main.Models.Transaction;
import com.ejsfbu.app_main.Models.User;
import com.ejsfbu.app_main.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ejsfbu.app_main.Models.Reward.checkCompletedGoals;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private Context context;
    private List<Request> requests;

    public RequestAdapter(Context context, List<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRequestType;
        private TextView tvRequestDetails;
        private Button bRequestApprove;
        private Button bRequestDeny;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestType = itemView.findViewById(R.id.tvRequestType);
            tvRequestDetails = itemView.findViewById(R.id.tvRequestDetails);
            bRequestApprove = itemView.findViewById(R.id.bRequestApprove);
            bRequestDeny = itemView.findViewById(R.id.bRequestDeny);
        }

        public void bind(Request request) {
            tvRequestType.setText(request.getRequestType());
            tvRequestDetails.setText(request.getRequestDetails());

            bRequestApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Transaction transaction = request.getTransaction();
                    transaction.setApproval(true);
                    transaction.setTransactionCompleteDate(new Date(System.currentTimeMillis()));
                    transaction.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                transaction.getBank().withdraw(transaction.getAmount());
                                Goal goal = transaction.getGoal();
                                goal.addSaved(transaction.getAmount());
                                goal.setUpdatesMade(true);
                                if (goal.getSaved() >= goal.getCost()) {
                                    goal.setCompleted(true);
                                    Date currentTime = Calendar.getInstance().getTime();
                                    goal.setDateCompleted(currentTime);
                                    goal.setEndDate(currentTime);
                                    goal.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                e.printStackTrace();
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                goal.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            request.deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        requests.remove(request);
                                                        notifyDataSetChanged();
                                                        Toast.makeText(context, "Request Completed",
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            bRequestDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Transaction transaction = request.getTransaction();
                    Goal goal = transaction.getGoal();
                    goal.removeTransaction(transaction);
                    goal.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                transaction.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            request.deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        requests.remove(request);
                                                        notifyDataSetChanged();
                                                        Toast.makeText(context,
                                                                "Request Cancelled",
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }
}
