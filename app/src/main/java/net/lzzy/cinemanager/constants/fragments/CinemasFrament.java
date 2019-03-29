package net.lzzy.cinemanager.constants.fragments;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
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


    //TextView textView = view.findViewById(R.id.fragment_cinemas_tv);

    @Override
    protected void populate() {
        lv = find(R.id.activity_main_lv);
        View empty = find(R.id.activity_main_layout_name);
        lv.setEmptyView(empty);
        cinemas=factory.get();
        GenericAdapter<Cinema> adapter=new GenericAdapter<Cinema>(getActivity(),
                R.layout.cinema_item,cinemas) {
            @Override
            public void populate(ViewHolder holder, Cinema cinema) {
                holder.setTextView(R.id.activity_cinema_item_name,cinema.getName())
                        .setTextView(R.id.activity_cinema_item_area,cinema.getLocation());
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

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }
}
