package com.example.totproject.party;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.totproject.R;
import com.example.totproject.chat.ChatRoomAdpter;
import com.example.totproject.chat.ChatRoomDTO;
import com.example.totproject.common.CommonAsk;
import com.example.totproject.common.CommonAskParam;
import com.example.totproject.common.CommonMethod;
import com.example.totproject.common.statics.Logined;
import com.example.totproject.login.SplashActivity;
import com.example.totproject.main.MainActivity;
import com.example.totproject.party_plan.PlanMainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPartyInfoActivity extends AppCompatActivity {
    RecyclerView rec_party_chat;
    Toolbar toolbar;
    Button partyinfo_btn_burger, btn_chat_push;
    EditText edt_chat;
    PartyListDTO plDTO;
    TextView party_title;
    LinearLayout lin_info_home_btn;



    ArrayList<PartyMemberListDTO> party_member_list = new ArrayList<>();


    //?????? ??? ?????????
    List<ChatRoomDTO> chatRoomDTOS;
    private RecyclerView.LayoutManager mLayoutManager;  //?????? ?????? ?????? ????????????
    private RecyclerView.Adapter chatMsgAdapter;    //?????? ?????? ?????? ????????????

    CommonAsk commonAsk;
    Gson gson = new Gson();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //????????? ??????????????? ?????? ??? 1
    private DatabaseReference databaseReference; //????????? ??????????????? ?????? ??? 2     // a = ?????????, b = ????????????
    public int updateParty = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_act_myparty_info);

        party_title = findViewById(R.id.partyinfo_tv_title);
        edt_chat = findViewById(R.id.edt_chat);
        btn_chat_push = findViewById(R.id.btn_chat_push);
        lin_info_home_btn = findViewById(R.id.lin_info_home_btn);



        Intent my_party_info_intent = getIntent();
        plDTO = (PartyListDTO) my_party_info_intent.getSerializableExtra("party_dto");
        selectPartyMemberList(plDTO.getParty_sn());       // ????????????????????? ???????????????

        // ???????????? ?????? ???????????????



        party_title.setText(plDTO.getParty_name());
        partyinfo_btn_burger = findViewById(R.id.partyinfo_btn_burger);

        lin_info_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPartyInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // ???????????? ???????????? ?????? ???????
        databaseReference = firebaseDatabase.getReference(plDTO.getParty_sn()+"");

        rec_party_chat = findViewById(R.id.rec_party_chat);
        rec_party_chat.setHasFixedSize(true);             // @@@@@@@?????? ????????? ????????
        mLayoutManager = new LinearLayoutManager(MyPartyInfoActivity.this);
        rec_party_chat.setLayoutManager(mLayoutManager);

        // ?????? ????????? ????????????
        chatRoomDTOS = new ArrayList<>();
        chatMsgAdapter = new ChatRoomAdpter(chatRoomDTOS , party_member_list, MyPartyInfoActivity.this );
        rec_party_chat.setAdapter(chatMsgAdapter);

        rec_party_chat.smoothScrollToPosition(chatRoomDTOS.size());


        btn_chat_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_chat.getText()+"";

                if (msg != null && !msg.equals("")) {
                    ChatRoomDTO dto = new ChatRoomDTO();
                    dto.setNickname(Logined.member_id); //????????? ??? ?????? ?????????(??????)
                    dto.setMsg(msg);
                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:aa");
                    String getTime = simpleDate.format(mDate);
                    dto.setDate(getTime);
                    databaseReference.push().setValue(dto);
                    edt_chat.setText("");
                    rec_party_chat.smoothScrollToPosition(chatRoomDTOS.size());

                }
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {      //~126 ?????? ?????????????????? ?????????????????? ???????????? ????????? ?????? ??????????????????
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatRoomDTO dto = snapshot.getValue(ChatRoomDTO.class);
                ((ChatRoomAdpter) chatMsgAdapter).addChat(dto);
              //  rec_party_chat.setAdapter(chatMsgAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        toolbar = (Toolbar) findViewById(R.id.partyinfo_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.partytinfo_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );//????????? ?????? ????????? ( ????????? ????????? ??????????????? ????????? ?????? )
        drawer.addDrawerListener(toggle);

        partyinfo_btn_burger = findViewById(R.id.partyinfo_btn_burger);
        partyinfo_btn_burger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                    drawer.openDrawer(Gravity.END);

            }
        });
        NavigationView nav_view = findViewById(R.id.partyinfo_burger_view);
        View nav_headerview = nav_view.getHeaderView(0);

        ImageView cancel;
        cancel = nav_headerview.findViewById(R.id.partyinfo_burger_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (drawer.isDrawerOpen(Gravity.END)) {
                    drawer.closeDrawer(Gravity.END);
                } else {
                    drawer.openDrawer(Gravity.END);
                }
            }
        });


        // ?????????????????????????????????????????????????????????      ?????? ????????????     ??????????????????????????????????????????????????????

        LinearLayout lin_leader_option, lin_member_option;
        TextView tv_logout;

        LinearLayout lin_mypartyburger_invite, lin_mypartyburger_delete, lin_mypartyburger_membermanage, lin_mypartyburger_update;
        LinearLayout lin_mypartyburger_invite2, lin_mypartyburger_delete2, lin_mypartyburger_membermanage2, lin_mypartyburger_update2;

        lin_leader_option = nav_headerview.findViewById(R.id.lin_leader_option);
        lin_member_option = nav_headerview.findViewById(R.id.lin_member_option);

        lin_mypartyburger_invite = nav_headerview.findViewById(R.id.lin_mypartyburger_invite);
        lin_mypartyburger_membermanage = nav_headerview.findViewById(R.id.lin_mypartyburger_membermanage);
        lin_mypartyburger_update = nav_headerview.findViewById(R.id.lin_mypartyburger_update);
        lin_mypartyburger_delete = nav_headerview.findViewById(R.id.lin_mypartyburger_delete);

        lin_mypartyburger_invite2 = nav_headerview.findViewById(R.id.lin_mypartyburger_invite2);
        lin_mypartyburger_membermanage2 = nav_headerview.findViewById(R.id.lin_mypartyburger_membermanage2);
        lin_mypartyburger_update2 = nav_headerview.findViewById(R.id.lin_mypartyburger_update2);
        lin_mypartyburger_delete2 = nav_headerview.findViewById(R.id.lin_mypartyburger_delete2);
        tv_logout = nav_headerview.findViewById(R.id.tv_logout);




        // ???????????? ????????? ?????? ?????? ???????????? ?????????
        if(Logined.member_id.equals(plDTO.getParty_leader())){
            lin_leader_option.setVisibility(View.VISIBLE);
            lin_member_option.setVisibility(View.INVISIBLE);
        }else{
            lin_leader_option.setVisibility(View.INVISIBLE);
            lin_member_option.setVisibility(View.VISIBLE);
        }

        // ???????????? ?????? ?????????
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = auto.edit();
                spEditor.clear();
                spEditor.commit();

                Intent intent = new Intent(MyPartyInfoActivity.this, SplashActivity.class);
                startActivity(intent);

            }
        });



        // ?????? ?????? ??????

        // ???????????? ?????? ?????? ?????????
        lin_mypartyburger_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPartyInfoActivity.this, "???????????? ?????? ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyPartyInfoActivity.this, InviteMemberActivity.class);
                intent.putExtra("plDTO",plDTO);
                startActivity(intent);


            }
        });

        // ???????????? ?????? ?????? ?????????
        lin_mypartyburger_membermanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPartyInfoActivity.this, "?????? ?????? ?????? ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyPartyInfoActivity.this, PartyMemberManageActivity.class);
                intent.putExtra("plDTO",plDTO);
                startActivity(intent);

            }
        });

        // ?????? ?????? ?????? ?????????
        lin_mypartyburger_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPartyInfoActivity.this, UpdateMyPartyActivity.class);
                intent.putExtra("plDTO",plDTO);
                startActivityForResult(intent , updateParty);

            }
        });

        // ???????????? ?????? ?????? ?????????
        lin_mypartyburger_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPartyInfoActivity.this, "???????????? ????????? ", Toast.LENGTH_SHORT).show();

                showCustomDialog("?????? ????????? ?????? ????????????????"); // alert ??????????????? ?????????


            }
        });



        // ?????? ?????? ??????

        // ???????????? ?????? ?????? ?????????
        lin_mypartyburger_invite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPartyInfoActivity.this, "???????????? ?????? ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyPartyInfoActivity.this, InviteMemberActivity.class);
                intent.putExtra("plDTO",plDTO);
                startActivity(intent);


            }
        });

        // ???????????? ?????? ?????? ?????????
        lin_mypartyburger_membermanage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPartyInfoActivity.this, "?????? ?????? ?????? ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyPartyInfoActivity.this, PartyMemberManageActivity.class);
                intent.putExtra("plDTO",plDTO);
                startActivity(intent);

            }
        });

        // ?????? ?????? ?????? ?????????
        lin_mypartyburger_update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPartyInfoActivity.this, PartyJoinActivity.class);
                intent.putExtra("party_sn",plDTO.getParty_sn());
                startActivity(intent);

            }
        });

        // ???????????? ?????? ?????? ?????????
        lin_mypartyburger_delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyPartyInfoActivity.this, "???????????? ????????? ", Toast.LENGTH_SHORT).show();

                showCustomDialog("?????? ????????? ??????????????????????"); // alert ??????????????? ?????????


            }
        });


        // ???????????? ????????????(?????????)
        TextView partyinfo_burger_tv_name = nav_headerview.findViewById(R.id.partyinfo_burger_tv_name);
        // ?????? ??????????????? ????????????
        partyinfo_burger_tv_name.setText(Logined.member_nick+"??? ???????????????");


        TextView partyinfo_burger_tv_grade = nav_headerview.findViewById(R.id.partyinfo_burger_tv_grade);
        // ?????? ?????? : ?????????, ?????????????????? ??????????????? ??????
        if(plDTO.getParty_leader().equals(Logined.member_id)){
            partyinfo_burger_tv_grade.setText("?????? : ????????????");
        }

        // ???????????? ?????? ??????
        CircleImageView partyinfo_burger_imgv_circle = nav_headerview.findViewById(R.id.partyinfo_burger_imgv_circle);
        if (Logined.picture_filepath != null){
            Glide.with(MyPartyInfoActivity.this).load(Logined.picture_filepath).into(partyinfo_burger_imgv_circle);
        }




        Menu nav_menu = nav_view.getMenu();

        // ???????????? ??????????????? ( ???, ??????, ????????? ?????????)
        NavigationView bottom_nav2;
        bottom_nav2 = findViewById(R.id.partyinfo_burger_view);
        bottom_nav2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);


                int id = item.getItemId();
                String tabText = (String) item.getTitle();
                String title = item.getTitle().toString();

                // @@@ ?????????????????? ???????????? ???????????? ???????????????

                if (id == R.id.partyinfo_burger_home) {
                    drawer.closeDrawer(Gravity.END);

                    // ?????? ??????
                } else if (id == R.id.partyinfo_burger_plan) {
                    Intent intent = new Intent(MyPartyInfoActivity.this, PlanMainActivity.class);
                    intent.putExtra("plDTO",plDTO);
                    intent.putExtra("tabcode",0);   //????????? ?????????
                    startActivity(intent);

                } else if (id == R.id.partyinfo_burger_myparty_list) {
                    Intent intent = new Intent(MyPartyInfoActivity.this,PartyMainActivity.class);
                    intent.putExtra("tabcode",3);
                    startActivity(intent);
                    finish();

                }


                return true;
            }
        });


    }//onCreate()


    public void showCustomDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPartyInfoActivity.this,
                R.style.AlertDialogTheme);

        View view= LayoutInflater.from(MyPartyInfoActivity.this).inflate(
            R.layout.common_alert_yes_or_no_item,
                (LinearLayout)findViewById(R.id.layout_alert)
        );

        //??????????????? ????????? ??????
        builder.setView(view);
        ((TextView)view.findViewById(R.id.texttitle)).setText("??? ??????");
        ((TextView)view.findViewById(R.id.textmessage)).setText(msg);
        ((TextView)view.findViewById(R.id.btn_dialog_yes)).setText("???");
        ((TextView)view.findViewById(R.id.btn_dialog_no)).setText("?????????");

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);   //????????? ????????? ??????????????? ????????????

        //??? ?????????
        view.findViewById(R.id.btn_dialog_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Logined.member_id.equals(plDTO.getParty_leader())){
                    deleteParty(plDTO); //?????? ?????? ??????
                }else{
                    deleteParty2(plDTO); //?????? ?????? ??????
                }


                alertDialog.dismiss();  //????????? ??????
                Intent intent = new Intent(MyPartyInfoActivity.this, PartyMainActivity.class);
                intent.putExtra("tabcode",3);
                startActivity(intent);

            }
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

    private void deleteParty(PartyListDTO plDTO) {
        commonAsk = new CommonAsk("android/party/deleteparty");
        commonAsk.params.add(new CommonAskParam("plDTO", gson.toJson(plDTO)) );
        InputStream in = CommonMethod.excuteAsk(commonAsk);

    }

    private void deleteParty2(PartyListDTO plDTO) {
        commonAsk = new CommonAsk("android/party/deleteParty2");
        commonAsk.params.add(new CommonAskParam("plDTO", gson.toJson(plDTO)) );
        InputStream in = CommonMethod.excuteAsk(commonAsk);

    }


    // party_sn?????? ?????? ????????????????????? ????????????
    public ArrayList<PartyMemberListDTO> selectPartyMemberList(int party_sn){
        commonAsk = new CommonAsk("android/party/planmemberlist");
        commonAsk.params.add(new CommonAskParam("party_sn", party_sn+""));
        InputStream in = CommonMethod.excuteAsk(commonAsk);

        try {
            party_member_list = gson.fromJson(new InputStreamReader(in), new TypeToken<List<PartyMemberListDTO>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return party_member_list;

    }//selectPartyList()







    public void ChangeActivity(Class nextAct, int tabcode, String tabText) {
        Intent intent = new Intent(MyPartyInfoActivity.this, nextAct);
        intent.putExtra("tabcode", tabcode);
        intent.putExtra("tabText", tabText);
        startActivity(intent);
        finish();
    }

    public void ChangeActivity(Class nextClass) {
        Intent intent = new Intent(MyPartyInfoActivity.this, nextClass);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK){
            plDTO = (PartyListDTO) data.getSerializableExtra("party_dto");
            String aaaa = "";
        }
    }







}