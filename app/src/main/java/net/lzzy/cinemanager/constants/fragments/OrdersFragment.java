package net.lzzy.cinemanager.constants.fragments;



import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment{

    private List<Order> orders;
    private OrderFactory factory;
    private GenericAdapter<Order> adapter;
    private Order order;
    public OrdersFragment() {
    }

    public OrdersFragment(Order order) {
        this.order = order;
    }
    @Override
    protected void populate() {
        ListView lv=find(R.id.activity_cinema_content_lv);
        View empty=find(R.id.dialog);
        lv.setEmptyView(empty);
        factory=OrderFactory.getInstance();
        orders=factory.get();
        adapter=new GenericAdapter<Order>(getActivity(),R.layout.cinema_item,orders) {
            @Override
            public void populate(ViewHolder holder, Order order) {
                String location = CinemaFactory.getInstance()
                        .getById(order.getCinemaId().toString()).toString();
                holder.setTextView(R.id.activity_cinema_item_name, order.getMovie())
                        .setTextView(R.id.activity_cinema_item_area, location);
            }

            @Override
            public boolean persistInsert(Order order) {
                return factory.addOrder(order);
            }

            @Override
            public boolean persistDelete(Order order) {
                return factory.delete(order);
            }
        };
        lv.setAdapter(adapter);
        if (order!=null){
            save(order);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_orders;
    }

    public void save(Order order) {
        adapter.add(order);
    }

    @Override
    public void search(String kw) {
        orders.clear();
        if (TextUtils.isEmpty(kw)){
            orders.addAll(factory.get());
        }else {
            orders.addAll(factory.searchOrders(kw));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        adapter.notifyDataSetChanged();
        super.onHiddenChanged(hidden);
    }
}
