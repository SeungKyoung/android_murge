package com.example.totproject.category;

//Recycler Adapter 마스터 ==> veiwPager2 ==>(Recycler Adapter )
//viewpager <가로만됨 > viewpager2 세로도됨.
//ViewHolder를 강제한다. ( 우리가 xml에 만들어놓은 위젯의 묶음이 되는 클래스 )
// Imageview , TextView <-묶어놓은 클래스를 반드시 만들고 이벤트 핸들링을 여기서해라.

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totproject.R;
import com.example.totproject.common.CommonAsk;
import com.example.totproject.common.CommonAskParam;
import com.example.totproject.common.CommonMethod;
import com.example.totproject.common.VO.BoardCommonVO;
import com.example.totproject.common.VO.PictureVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
//2.아이템마다 보여줄 정보를 가지고있는 DTO를 하나로 묶은 Collection 구조를 받을수있게
//필드로 선언 , LayoutInflater 사용을 위한 준비를 함. <-(1).Activity로부터 받아옴(Context)
    //(2) static으로 있는 getApplicationContext로 사용 <- 지원안하는 기능들도있음 ↑
    //context<- 토스트를 띄우기 , LayoutInflater만들기 , 등등 화면에 떠있는 객체에서
    //할수있는 기능들을 처리할수가있다. (일반 클래스에도 context를 넘기면 화면 제어를 할수가있음)
    //RecAdapter<- 일반 클래스임

    //메인에서 넘겨 받을 필드
    Context context;
    ArrayList<BoardCommonVO> list;
    LayoutInflater inflater;

    CommonAsk commonAsk;
    Gson gson = new Gson();

    //생성자 메소드를 만듬 ↑ 세개를 입력받거나 또는 두개를 입력받아서 LayoutInflater까지 null이 아니게 ..
    //생성자 메소드 호출해보기 . ( RecyclerActivity )


    public RecAdapter(Context context, ArrayList<BoardCommonVO> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //액티비티의 소스 줄을 조금 줄일수가있음.
        //context 자체를 받아왔기때문에 context를 이용하는것이 메모리에 더 효율적이다.
    }

    public RecAdapter(Context context) {
        this.context = context;
    }


    //3.화면을 인플레이트 시킨다. ( 리사이클러의 아이템 하나마다 붙을 xml을 연결시킨다 )
    // xml 로 연결시킨 view자체를 Viewholder에 넘겨줌 ( == ViewHolder에서는 view를 통해서 find )
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = inflater.inflate(R.layout.category_frag_detail_item_review , parent , false );
       //1. ViewHolder holder = new ViewHolder(itemview);
       // return holder;
        return new ViewHolder(itemview);
    }
    //4. 아이템이 ↑ 세팅되고 나서의 처리를 의미함↓
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*BoardCommonVO vo = new BoardCommonVO();
        holder.tv_username.setText(vo.getMember_id());
        holder.tv_content.setText(vo.getBoard_content());*/
        holder.tv_content.setText(list.get(position).getBoard_content());
        holder.tv_username.setText(list.get(position).getMember_nick());

        holder.getImageURL(position);
    }
    //5.↓ 총 아이템의 갯수를 지정함.
    @Override
    public int getItemCount() {
        return list.size();
    }

    //1. RecyclerView.ViewHolder 상속을 받은 클래스 ViewHolder를 만들어줌
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView category_detail_img1, category_detail_img2 , category_detail_img3;
        TextView tv_username, tv_content; //xml에 있는 위젯들을 전역변수로 선언.
        LinearLayout image_parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_detail_img1 = itemView.findViewById(R.id.category_detail_img1);
            category_detail_img2 = itemView.findViewById(R.id.category_detail_img2);
            category_detail_img3 = itemView.findViewById(R.id.category_detail_img3);
            tv_username = itemView.findViewById(R.id.category_detail_tv_username);
            tv_content = itemView.findViewById(R.id.category_detail_tv_contenttv);
            category_detail_img1.setScaleType(ImageView.ScaleType.FIT_XY);
            category_detail_img2.setScaleType(ImageView.ScaleType.FIT_XY);
            category_detail_img3.setScaleType(ImageView.ScaleType.FIT_XY);

            image_parent = itemView.findViewById(R.id.image_parent);
        }

        public void getImageURL(int position){
            commonAsk = new CommonAsk("list_picture");
            commonAsk.params.add(new CommonAskParam("board_sn", String.valueOf(list.get(position).getBoard_sn())));
            InputStream in = CommonMethod.excuteAsk(commonAsk);
            ArrayList<PictureVO> getList = gson.fromJson(new InputStreamReader(in), new TypeToken<List<PictureVO>>(){}.getType());

            Log.i("asdasd??",getList.size()+"");
            try {
                int size = getList.size();
                switch (size){
                    case 0:
                        image_parent.setVisibility(View.GONE);
                        break;
                    case 1:
                        category_detail_img2.setVisibility(View.INVISIBLE);
                        category_detail_img3.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        category_detail_img3.setVisibility(View.INVISIBLE);
                        break;
                }
                for (int i = 0; i < getList.size();i++){
                    switch (i){
                        case 0:
                            Glide.with(context)
                                    .load(getList.get(i).getPicture_filepath())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(category_detail_img1);
                            break;
                        case 1:
                            Glide.with(context)
                                    .load(getList.get(i).getPicture_filepath())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(category_detail_img2);
                            break;
                        case 2:
                            Glide.with(context)
                                    .load(getList.get(i).getPicture_filepath())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(category_detail_img3);
                            break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
