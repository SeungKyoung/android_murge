package com.example.totproject.mainburgeractivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.totproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainBurger03ManualFg extends Fragment {
    BottomNavigationView manualNav;
    TextView title, content;

    Context context;
    FragmentManager manager;


    public MainBurger03ManualFg(Context context, FragmentManager manager) { //컨텍슽르르 메인에서부터 가져옴
        this.context = context;
        this.manager = manager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mainburger03_manual_fg, container, false);
        manualNav = v.findViewById(R.id.nav_manualnav);

        title = v.findViewById(R.id.manual_tv_title);
        content = v.findViewById(R.id.manual_tv_content);


        title.setText("trip or travel 이용약관");
        content.setText("여러분을 환영합니다. ");
        manualNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.manualnav_left) {
                    title.setText("trip or travel 이용약관");
                    content.setText("\n다양한 [ToT]  서비스를 즐겨보세요. \n회원으로 가입하시면 [ToT]  서비스를 보다 편리하게 이용할 수 있습니다. \n여러분이 제공한 콘텐츠를 소중히 다룰 것입니다. \n여러분의 개인정보를 소중히 보호합니다. \n타인의 권리를 존중해 주세요. \n[ToT]  서비스 이용과 관련하여 몇 가지 주의사항이 있습니다. \n[ToT] 에서 제공하는 다양한 포인트를 요긴하게 활용해 보세요. \n부득이 서비스 이용을 제한할 경우 합리적인 절차를 준수합니다. \n[ToT] 의 잘못은 [ToT] 가 책임집니다. \n일부 [ToT]  서비스에는 광고가 포함되어 있습니다. \n언제든지 [ToT]  서비스 이용계약을 해지하실 수 있습니다. \n서비스 중단 또는 변경 시 꼭 알려드리겠습니다. \n주요 사항을 잘 안내하고 여러분의 소중한 의견에 귀 기울이겠습니다. \n여러분이 쉽게 알 수 있도록 약관 및 운영정책을 게시하며 사전 공지 후 개정합니다. ");

                    return true;
                } else if (item.getItemId() == R.id.manualnav_right) {
                    title.setText("trip or travel 정책");
                    content.setText("[ToT] 는 다양한 정보와 의견이 담긴 여러분의 게시물이 다른 분들에게 전달되어 우리 모두의 삶을 더욱 풍요롭게 해 줄 것을 기대합니다. \n [ToT] 는 여러분이 자유롭게 작성한 게시물을 소중히 다룰 것입니다. \n\n게시물이란 이용자가 타인 또는 자신이 보게 할 목적으로 [ToT]  서비스 상에 게재한 부호, 문자, 음성, 음향, 그림, 사진, 동영상, 링크 등으로 구성된 각종 콘텐츠 자체 또는 파일을 말합니다. \n\n본 게시물 운영정책을 통해 회원 가입 이후 [ToT]  서비스 이용 과정에서 자신의 게시물을 어떻게 관리해야 하는지 확인해 주세요. \n\n부득이 일정한 내용의 게시물은 게재가 제한될 수 있습니다. \n여러분의 다양한 생각이나 경험 또는 의견이 담긴 게시물은 최대한 게재가 유지되어 다른 이용자들에게 전파될 것입니다. \n 다만 여러분이 [ToT]  서비스를 보다 안전하게 이용할 수 있도록, [ToT]  서비스에서 여러분과 타인의 권리가 서로 존중되고 보호받을 수 있도록, 그리고 [ToT]  서비스가 신뢰 있는 서비스로서 안정적으로 제공될 수 있도록 부득이 아래와 같은 경우 여러분의 게시물 게재가 제한될 수 있으므로, 이에 대한 확인 및 준수를 요청 드립니다. \n\n관련 법령을 위반하거나 타인의 권리를 침해하는 내용의 게시물은 명백한 법령 위반 또는 권리 침해의 내용이 아닌 한, 원칙적으로 법원의 판결, 결정 등 또는 법률에 따라 관련 권한을 보유한 행정기관의 행정처분, 명령 등에 의해 법령 위반 또는 권리 침해가 확인된 경우 게재가 제한됨을 알려 드립니다. \n");

                    return true;
                }

                return false;
            }
        });


        return v;
    }


}