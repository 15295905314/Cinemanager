package net.lzzy.cinemanager.constants.fragments;

import android.content.Context;
import android.media.audiofx.AudioEffect;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/3.
 * Description:
 */
public class CinemaOrdersFragment extends BaseFragment{
    private String cinemaId;

    private  static final String ARG_CINEMA_ID="argCinemaId";

    public  static CinemaOrdersFragment  newInstance(String cinemaId){
        CinemaOrdersFragment fragment=new CinemaOrdersFragment();
        Bundle args=new Bundle();
        args.putString(ARG_CINEMA_ID,cinemaId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            cinemaId=getArguments().getString(ARG_CINEMA_ID);
        }
    }

    @Override
    protected void populate() {
        ListView lv=find(R.id.activity_main_lv);//_orders_lv
        View empty = find(R.id.activity_main_layout_name);//_orders_none
        lv.setEmptyView(empty);
        List<Order> orders = OrderFactory.getInstance().getOrdersByCinema(cinemaId);
        GenericAdapter<Order> adapter=new GenericAdapter<Order>(getActivity(),R.layout.cinema_item,orders) {
            @Override
            public void populate(ViewHolder holder, Order order) {
                holder.setTextView(R.id.activity_cinema_item_name,order.getMovie())
                        .setTextView(R.id.activity_cinema_item_area,order.getMovieTime());

            }
            //cinema_item_tv_name
            //cinema_item_tv_location

            @Override
            public boolean persistInsert(Order order)
            {
                return false;
            }

            @Override
            public boolean persistDelete(Order order)
            {
                return false;
            }
        };
        lv.setAdapter(adapter);


    }


    @Override
    public int getLayoutRes() {

        return R.layout.fragment_cinema_orders;
    }

    @Override
    public void search(String kw) {

    }
}
