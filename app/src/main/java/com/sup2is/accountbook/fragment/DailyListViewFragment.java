package com.sup2is.accountbook.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sup2is.accountbook.R;
import com.sup2is.accountbook.adapter.DailyRecycleViewAdapter;
import com.sup2is.accountbook.application.AccountBookApplication;
import com.sup2is.accountbook.database.DBManager;
import com.sup2is.accountbook.databinding.FragmentDailyListviewBinding;
import com.sup2is.accountbook.model.Account;
import com.sup2is.accountbook.model.DateBundle;
import com.sup2is.accountbook.util.GlobalDate;

import java.util.ArrayList;

public class DailyListViewFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DailyListViewFragment.class.getSimpleName();

    private GlobalDate globalDate;

    private FragmentDailyListviewBinding dailyListviewBinding;

    private AccountBookApplication application;

    private DBManager dbManager;

    private DailyRecycleViewAdapter dailyRecycleViewAdapter;
    private ArrayList<Account> accounts ;
    private boolean isVisible = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_daily_listview,container,false);
        dailyListviewBinding = DataBindingUtil.bind(view);
        DateBundle temp = new DateBundle(
                String.valueOf(globalDate.getYear()),
                String.valueOf(globalDate.getMonth()),
                String.valueOf(globalDate.getDay()),
                null,"0","0","0");

        application = (AccountBookApplication) getActivity().getApplication();
        dbManager = application.getDbManager();
        accounts = dbManager.selectByDate(temp);

        dailyRecycleViewAdapter = new DailyRecycleViewAdapter(getContext(),accounts);
        dailyListviewBinding.rvDailyList.setLayoutManager(new LinearLayoutManager(getContext()));
        dailyListviewBinding.rvDailyList.setAdapter(dailyRecycleViewAdapter);

        dailyListviewBinding.fabInput.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        globalDate = GlobalDate.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalDate = GlobalDate.getInstance();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        Fragment currentDialog = fm.findFragmentByTag("input");
        if(currentDialog == null) {
            InputFormDialogFragment inputFormDialogFragment = new InputFormDialogFragment();
            inputFormDialogFragment.show(fm,"input");
        }
    }

    private View noDataView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if(getView() != null) {
                isVisible = true;
                DateBundle temp = new DateBundle(
                        String.valueOf(globalDate.getYear()),
                        String.valueOf(globalDate.getMonth()),
                        null,
                        null,null,null,null);
                accounts = dbManager.selectByDate(temp);
                if(accounts.size() == 0 || accounts == null) {
                    dailyListviewBinding.rvDailyList.removeAllViews();
                    dailyListviewBinding.ivNodata.setVisibility(View.VISIBLE);
                    dailyRecycleViewAdapter.clearList();
                }else {
                    dailyListviewBinding.ivNodata.setVisibility(View.GONE);
                    dailyRecycleViewAdapter.updateList(accounts);
                }
            }else {
                isVisible = false;
            }
        }
    }
}
