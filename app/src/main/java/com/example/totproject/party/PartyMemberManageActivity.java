package com.example.totproject.party;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.totproject.R;
import com.example.totproject.common.CommonAsk;
import com.example.totproject.common.CommonAskParam;
import com.example.totproject.common.CommonMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PartyMemberManageActivity extends AppCompatActivity {
    RecyclerView rec_member_list;
    ArrayList<PartyMemberListDTO> member_list = new ArrayList<>();
    ArrayList<PartyMemberListDTO> delete_list = new ArrayList<>();
    CommonAsk commonAsk;
    Gson gson = new Gson();

    ImageView imgv_partyleader_pic;
    TextView tv_partyleader_name;
    Button btn_party_memberdelete;

    LinearLayout lin_party_memberdelete, lin_manage_title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_act_membermanage);

        rec_member_list = findViewById(R.id.rec_member_list);
        imgv_partyleader_pic =findViewById(R.id.imgv_partyleader_pic);
        tv_partyleader_name =findViewById(R.id.tv_partyleader_name);
        btn_party_memberdelete =findViewById(R.id.btn_party_memberdelete);
        lin_party_memberdelete =findViewById(R.id.lin_party_memberdelete);
        lin_manage_title =findViewById(R.id.lin_manage_title);




        Intent getIntent = getIntent();
        PartyListDTO plDTO = (PartyListDTO) getIntent.getSerializableExtra("plDTO");
        showPartyMember(plDTO);

        if (member_list != null){

            for (int i =0; i<member_list.size(); i++){
                // ????????? ?????? ??????
                if (member_list.get(i).getMemberid().equals(plDTO.getParty_leader())){
                    Glide.with(PartyMemberManageActivity.this).load(member_list.get(i).getPicture_filepath()).into(imgv_partyleader_pic);
                    tv_partyleader_name.setText(member_list.get(i).getMemberid());
                    member_list.remove(i);  // ????????? ?????? ???????????? ????????????????????? ????????? (????????? ???????????????)
                }
            }

            // ????????? ?????? ??????????????? ??? ??????
            PartymemberManageAdapter partymemberManageAdapter = new PartymemberManageAdapter(plDTO,PartyMemberManageActivity.this, member_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    PartyMemberManageActivity.this , RecyclerView.VERTICAL , false
            );
            rec_member_list.setLayoutManager(layoutManager);
            rec_member_list.setAdapter(partymemberManageAdapter);
        }else{
            Toast.makeText(PartyMemberManageActivity.this, "????????? ???????????? ???????????? (??????)", Toast.LENGTH_SHORT).show();
        }


        // ?????? ????????? ????????????
        lin_manage_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//????????? ??????
                overridePendingTransition(0, 0);//????????? ?????? ?????????
                Intent intent = getIntent(); //?????????
                startActivity(intent); //???????????? ??????
                finish();
                overridePendingTransition(0, 0);//????????? ?????? ?????????
            }
        });





    }//onCreate()


    public ArrayList<PartyMemberListDTO> showPartyMember(PartyListDTO plDTO){
        commonAsk = new CommonAsk("android/party/showpartymember");
        commonAsk.params.add(new CommonAskParam("plDTO",gson.toJson(plDTO)));
        InputStream in = CommonMethod.excuteAsk(commonAsk);

        try {
            member_list = gson.fromJson(new InputStreamReader(in), new TypeToken<List<PartyMemberListDTO>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return member_list;

    }//showMyPartylist()



    // ??? ????????? ?????? ??????
    public void test(PartyListDTO plDTO){


        // ???????????? ?????????????????????
        for(int i  = 0 ; i< rec_member_list.getChildCount(); i++){
            PartymemberManageAdapter.Viewholder viewholder = (PartymemberManageAdapter.Viewholder) rec_member_list.findViewHolderForAdapterPosition(i);
            viewholder.lin_chk_box.setVisibility(View.VISIBLE);

        }


        //?????? ?????? ?????????
        btn_party_memberdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(plDTO);

            }
        });

    }//test()



    // ???????????? ????????? ?????????????????? ??????
    private void deletePartyMember(@NonNull PartyListDTO plDTO, ArrayList<PartyMemberListDTO> list) {
        commonAsk = new CommonAsk("android/party/deletepartymember");// list = []  vo ={ }
        String data = gson.toJson(list);
        commonAsk.params.add(new CommonAskParam("list" , data));
        commonAsk.params.add(new CommonAskParam("party_sn" , plDTO.getParty_sn()+""));

        InputStream in = CommonMethod.excuteAsk(commonAsk);


    }

    public void showCustomDialog(PartyListDTO plDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartyMemberManageActivity.this,
                R.style.AlertDialogTheme);

        View view= LayoutInflater.from(PartyMemberManageActivity.this).inflate(
                R.layout.common_alert_yes_or_no_item,
                (LinearLayout)findViewById(R.id.layout_alert)
        );

        //??????????????? ????????? ??????
        builder.setView(view);
        ((TextView)view.findViewById(R.id.texttitle)).setText("??? ??????");
        ((TextView)view.findViewById(R.id.textmessage)).setText("?????? ?????? ????????? ??????????????????????");
        ((TextView)view.findViewById(R.id.btn_dialog_yes)).setText("???");
        ((TextView)view.findViewById(R.id.btn_dialog_no)).setText("?????????");

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);   //????????? ????????? ??????????????? ????????????

        //??? ?????????
        view.findViewById(R.id.btn_dialog_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();  //????????? ??????

                // ???????????? ???????????? ~
                if(delete_list.size() == 0 ){
                    return;
                }
                deletePartyMember(plDTO,delete_list);

                //???????????? ??????
                finish();//????????? ??????
                overridePendingTransition(0, 0);//????????? ?????? ?????????
                Intent intent = getIntent(); //?????????
                startActivity(intent); //???????????? ??????
                finish();
                overridePendingTransition(0, 0);//????????? ?????? ?????????

            }
            // ~ ???????????? ????????????

        });

        //????????? ?????????
        view.findViewById(R.id.btn_dialog_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();  //????????? ??????
            }
        });

        //??????????????? ?????? ?????????
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();



    }//showCustomDialog()




}