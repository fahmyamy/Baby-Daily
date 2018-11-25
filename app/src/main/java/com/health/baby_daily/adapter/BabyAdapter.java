package com.health.baby_daily.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.health.baby_daily.BabyActivity;
import com.health.baby_daily.BabyEdit;
import com.health.baby_daily.BabyProfile;
import com.health.baby_daily.R;
import com.health.baby_daily.guide.BabyReport;
import com.health.baby_daily.model.Baby;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class BabyAdapter extends RecyclerView.Adapter<BabyAdapter.BabyViewHolder> {

    private Context context;
    private List<Baby> babyList;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private int totalday, totalmonth, totalyear;
    private String role;

    public BabyAdapter(Context context, List<Baby> babyList) {
        this.context = context;
        this.babyList = babyList;
    }

    @NonNull
    @Override
    public BabyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_baby_list, null);
        return new BabyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BabyViewHolder babyViewHolder, int position) {
        final Baby baby = babyList.get(position);
        final String bId = baby.getbId();
        babyViewHolder.babyName.setText(baby.getFullName());

        SharedPreferences prefRole = context.getSharedPreferences("currRole", 0);
        role = prefRole.getString("role", null);

        //age counting
        String dob = baby.getDob();
        String[] datesplit = dob.split("/");
        int day = Integer.parseInt(datesplit[0]);
        int month = Integer.parseInt(datesplit[1]);
        int year = Integer.parseInt(datesplit[2]);

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentdate = dateFormat.format(calendar.getTime());
        String[] currsplit = currentdate.split("/");
        int currday = Integer.parseInt(currsplit[0]);
        int currmonth = Integer.parseInt(currsplit[1]);
        int curryear = Integer.parseInt(currsplit[2]);

        //subtract date
        if ((currday == day) || (currday > day)) {
            totalday = currday - day;
        } else if (currday < day) {
            currday = currday + 31;
            totalday = currday - day;
            currmonth = currmonth - 1;
        }

        if ((currmonth == month) || (currmonth > month)) {
            totalmonth = currmonth - month;
            totalyear = curryear - year;
        } else if (currmonth < month) {
            currmonth = currmonth + 12;
            totalmonth = currmonth - month;
            curryear = curryear - 1;
            totalyear = curryear - year;
        }

        final String age = totalyear + " Years " + totalmonth + " Months " + totalday + " Days ";
        babyViewHolder.babyAge.setText(age);

        //popupmenu for baby mgt
        if (role.equals("Mother") || role.equals("Father")){
            babyViewHolder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_baby, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int item = menuItem.getItemId();

                            if (item == R.id.action_update) {
                                Intent intent = new Intent(context, BabyEdit.class);
                                intent.putExtra("babyId", bId);
                                context.startActivity(intent);
                                return true;
                            } else if (item == R.id.action_delete) {
                                firebaseStorage = FirebaseStorage.getInstance();
                                storageReference = firebaseStorage.getReference();
                                StorageReference ref = storageReference.child("babies/" + bId);
                                ref.delete();

                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Baby").child(bId);
                                databaseReference.removeValue();
                                Toast.makeText(context, "Baby Info Removed!!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, BabyActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                                return true;
                            }

                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }else if (role.equals("Caregiver")){
            babyViewHolder.overflow.setVisibility(View.GONE);
        }else if (role.equals("Doctor")){
            babyViewHolder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_baby2, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int item = menuItem.getItemId();

                            if (item == R.id.action_view) {
                                Intent intent = new Intent(context, BabyProfile.class);
                                intent.putExtra("babyId", bId);
                                context.startActivity(intent);
                                return true;
                            } else if (item == R.id.action_record) {
                                Intent intent = new Intent(context, BabyReport.class);
                                context.startActivity(intent);

                                SharedPreferences pref = context.getSharedPreferences("selectedBaby", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("babyId", bId);
                                editor.apply();
                                return true;
                            }

                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        //baby profile
        babyViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bId.isEmpty()){

                }else {
                    Intent intent = new Intent(context, BabyProfile.class);
                    intent.putExtra("babyId", bId);
                    context.startActivity(intent);
                }
            }
        });

        //baby picture
        if(!baby.getImage().equals("none")){
            Picasso.get().load(baby.getImage()).into(babyViewHolder.thumbnail);
        }else {
            Picasso.get().load(R.drawable.user_image).into(babyViewHolder.thumbnail);
        }


    }

    @Override
    public int getItemCount() {
        return babyList.size();
    }

    class BabyViewHolder extends RecyclerView.ViewHolder{
        TextView babyName, babyAge;
        ImageView thumbnail, overflow;

        BabyViewHolder(@NonNull View itemView) {
            super(itemView);

            babyName = itemView.findViewById(R.id.babyName);
            babyAge = itemView.findViewById(R.id.babyAge);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            overflow = itemView.findViewById(R.id.overflow);
        }
    }
}
