package com.example.totproject.party_plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.totproject.R;
import com.example.totproject.common.CommonAsk;
import com.example.totproject.common.CommonAskParam;
import com.example.totproject.common.CommonMethod;
import com.example.totproject.party.PartyJoinActivity;
import com.example.totproject.party.PartyMemberListDTO;
import com.example.totproject.party.PartymemberListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class PlanInfo01Fragment extends Fragment {
    Context context;
    PlanMainActivity planMainActivity = new PlanMainActivity();
    PlanlistDTO planlistDTO;

    GridView grid_memberlist;
    ArrayList<PartyMemberListDTO> plan_member_list = new ArrayList<>();
    CommonAsk commonAsk;
    Gson gson = new Gson();





    TextView tv_plan_startdate, tv_plan_starttime, tv_plan_enddate, tv_plan_endtime, tv_plan_location, tv_plan_startpoint,
            tv_plan_hotel, tv_plan_cost;

    Button btn_plan_info02, btn_planmain_update,btn_planmain_create;


    public PlanInfo01Fragment(Context context, PlanlistDTO planlistDTO) {
        this.context = context;
        this.planlistDTO = planlistDTO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.partyplan_frag_planinfo01, container, false);

        planMainActivity = (PlanMainActivity) getActivity();


        btn_plan_info02 = view.findViewById(R.id.btn_plan_info02);

        grid_memberlist = view.findViewById(R.id.grid_memberlist);

        tv_plan_startdate=view.findViewById(R.id.tv_plan_startdate);
        tv_plan_starttime=view.findViewById(R.id.tv_plan_starttime);
        tv_plan_enddate=view.findViewById(R.id.tv_plan_enddate);
        tv_plan_endtime=view.findViewById(R.id.tv_plan_endtime);
        tv_plan_location=view.findViewById(R.id.tv_plan_location);
        tv_plan_startpoint=view.findViewById(R.id.tv_plan_startpoint);
        tv_plan_hotel=view.findViewById(R.id.tv_plan_hotel);
        tv_plan_cost=view.findViewById(R.id.tv_plan_cost);
        //@@
        //btn_planmain_create=getActivity().findViewById(R.id.btn_planmain_create);
        //btn_planmain_update=getActivity().findViewById(R.id.btn_planmain_update);

        // ???????????? ??????????????????, ???????????? ?????? ?????????     @@
//        btn_planmain_create.setVisibility(View.GONE);
//        btn_planmain_update.setVisibility(View.VISIBLE);

        planMemberListNew(planlistDTO.getPlan_sn());




        // ????????? ????????????
        PartymemberListAdapter adapter = new PartymemberListAdapter(getActivity(), plan_member_list);
        grid_memberlist = view.findViewById(R.id.grid_memberlist);
        grid_memberlist.setAdapter(adapter);
        setDynamicHeight(grid_memberlist);

        //@@@
        //btn_planmain_update.setText("????????????");
        tv_plan_startdate.setText(planlistDTO.getPlan_startdate());
        tv_plan_starttime.setText(planlistDTO.getPlan_starttime());
        tv_plan_enddate.setText(planlistDTO.getPlan_enddate());
        tv_plan_endtime.setText(planlistDTO.getPlan_endtime());
        tv_plan_location.setText(planlistDTO.getPlan_location());
        tv_plan_startpoint.setText(planlistDTO.getPlan_startpoint());
        tv_plan_hotel.setText(planlistDTO.getPlan_hotel());
        tv_plan_cost.setText(planlistDTO.getPlan_cost());


        //?????? ?????? ????????? ??????
        btn_plan_info02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanInfo02Fragment plan_info_frag02 = new PlanInfo02Fragment(getActivity(),planlistDTO);
                planMainActivity.changePlanFrag(plan_info_frag02 , planlistDTO.getPlan_name());

            }
        });

        //@@@@@
//        btn_planmain_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), PlanUpdatePlanActivity.class);
//                intent.putExtra("planlistDTO",planlistDTO);
//                getActivity().startActivity(intent);
//            }
//        });




        return view;
    }//onCreateView()


    //?????? ??????????????? ?????? (??????????????? ???????????? ????????? ?????????)
    public ArrayList<PartyMemberListDTO> planMemberListNew(int plan_sn) {
        commonAsk = new CommonAsk("android/party/planMemberListNew");
        commonAsk.params.add(new CommonAskParam("plan_sn",plan_sn+""));

        InputStream in = CommonMethod.excuteAsk(commonAsk);

        // ???????????? ?????? ???????????????

        try {
            plan_member_list = gson.fromJson(new InputStreamReader(in), new TypeToken<List<PartyMemberListDTO>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plan_member_list;
    }


    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > 2 ){
            x = items/2;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight -50;
        gridView.setLayoutParams(params);
    }

}