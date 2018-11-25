package com.health.baby_daily.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.health.baby_daily.R;
import com.health.baby_daily.chatfragment.CGFragment;
import com.health.baby_daily.chatfragment.ChatFragment;
import com.health.baby_daily.chatfragment.DoctorFragment;
import com.health.baby_daily.chatfragment.FriendFragment;
import com.health.baby_daily.contact.ContactList;
import com.health.baby_daily.contact.SearchFriend;
import com.health.baby_daily.contact.SearchParent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatFrag extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FloatingActionButton fab_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment,container, false);
        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_btn = view.findViewById(R.id.fab_btn);
        fab_btn.setIcon(R.drawable.ic_chat_black_24dp);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                SharedPreferences prefRole = Objects.requireNonNull(getActivity()).getSharedPreferences("currRole", 0);
                String role = prefRole.getString("role", null);
                switch (i){
                    case 0:
                        fab_btn.setIcon(R.drawable.ic_chat_black_24dp);
                        fab_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(view.getContext(), ContactList.class));
                            }
                        });
                        break;

                    case 1:
                        if (role.equals("Mother") || role.equals("Father")){
                            fab_btn.setIcon(R.drawable.ic_heart2);
                        }else if (role.equals("Caregiver") || role.equals("Doctor")){
                            fab_btn.setIcon(R.drawable.ic_add_person);
                        }
                        fab_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(view.getContext(), SearchParent.class));
                            }
                        });
                        break;

                    case 2:
                        if (role.equals("Mother") || role.equals("Father")){
                            fab_btn.setIcon(R.drawable.ic_add_person);
                            fab_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(view.getContext(), SearchParent.class));
                                }
                            });
                        }else if (role.equals("Doctor")){
                            fab_btn.setIcon(R.drawable.ic_add_person);
                            fab_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(view.getContext(), SearchFriend.class));
                                }
                            });
                        }

                        break;
                }
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        SharedPreferences prefRole = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            prefRole = Objects.requireNonNull(getActivity()).getSharedPreferences("currRole", 0);
            String role = prefRole.getString("role", null);
            if (role.equals("Mother") || role.equals("Father")){
                adapter.addFragment(new ChatFragment(), "Chat");
                adapter.addFragment(new CGFragment(), "Caregiver");
                adapter.addFragment(new DoctorFragment(), "Doctor");
            }else if (role.equals("Caregiver")){
                adapter.addFragment(new ChatFragment(), "Chat");
                adapter.addFragment(new CGFragment(), "Parent");
            }else if (role.equals("Doctor")){
                adapter.addFragment(new ChatFragment(), "Chat");
                adapter.addFragment(new DoctorFragment(), "Parent");
                adapter.addFragment(new FriendFragment(), "Friend");
            }
        }

        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
