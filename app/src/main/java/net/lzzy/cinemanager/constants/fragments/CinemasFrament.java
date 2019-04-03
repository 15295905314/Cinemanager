package net.lzzy.cinemanager.constants.fragments;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.activities.CinemaOrdersActivity;
import net.lzzy.cinemanager.activities.MainActivity;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;


/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class CinemasFrament extends BaseFragment {
    private List<Cinema> cinemas;
    private CinemaFactory factory = CinemaFactory.getInstance();
    private ListView lv;
    private Cinema cinema;
    private onCinemaSelectedListener listener;

    private GenericAdapter<Cinema> adapter;

    public CinemasFrament() {
    }

    public CinemasFrament(Cinema cinema) {
        this.cinema = cinema;
    }


    //TextView textView = view.findViewById(R.id.fragment_cinemas_tv);

    @Override
    protected void populate() {
        lv = find(R.id.activity_main_lv);
        View empty = find(R.id.activity_main_layout_name);
        lv.setEmptyView(empty);
        cinemas = factory.get();
        adapter = new GenericAdapter<Cinema>(getActivity(),
                R.layout.cinema_item, cinemas) {
            @Override
            public void populate(ViewHolder holder, Cinema cinema) {
                holder.setTextView(R.id.activity_cinema_item_name, cinema.getName())
                        .setTextView(R.id.activity_cinema_item_area, cinema.getLocation());
            }

            @Override
            public boolean persistInsert(Cinema cinema) {
                return factory.addCinema(cinema);
            }

            @Override
            public boolean persistDelete(Cinema cinema) {
                return factory.deleteCinema(cinema);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) ->{
           listener.OnCinemsSelected(adapter.getItem(position).getId().toString());

        } );
        if (cinema != null) {
            svae(cinema);
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }

    @Override
    public void search(String kw) {
        cinemas.clear();
        if (TextUtils.isEmpty(kw)) {
            cinemas.addAll(factory.get());
        } else {
            cinemas.addAll(factory.searchCinemas(kw));
        }
        adapter.notifyDataSetChanged();
    }

    public void svae(Cinema cinema) {
        adapter.add(cinema);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (onCinemaSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现onCinemaSelectedListener接口");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public interface onCinemaSelectedListener {
        void OnCinemsSelected(String cinemaId);
    }

}
