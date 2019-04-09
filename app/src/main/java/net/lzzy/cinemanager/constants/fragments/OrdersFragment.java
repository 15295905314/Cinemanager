package net.lzzy.cinemanager.constants.fragments;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.cinemanager.utils.ViewUtils;
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
    float touchX1;
    float touchX2;
    boolean isDelete = false;
    private final float MNT_DISTANCE = 100;
    //public OrderFactory orderFactory=OrderFactory.getInstance();
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

                Button button=holder.getView(R.id.activity_cinema_btn);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("删除确认")
                                .setMessage("要删除订单吗？")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isDelete=false;
                                        adapter.remove(order);
                                    }
                                }).show();
                    }
                });
                int visibility=isDelete?View.VISIBLE:View.GONE;
                button.setVisibility(visibility);
                holder.getConvertView().setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
                    @Override
                    public boolean handleTouch(MotionEvent event) {
                        slideToDelete(event,order,button);
                        return true;
                    }
                });

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
    private void slideToDelete(MotionEvent event, Order order, Button btn) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                touchX2 = event.getX();
                if (touchX1 - touchX2 > MNT_DISTANCE) {
                    if (!isDelete) {
                        btn.setVisibility(View.VISIBLE);
                        isDelete = true;
                    }
                } else {
                    if (btn.isShown()) {
                        btn.setVisibility(View.GONE);
                        isDelete = false;
                    } else {
                        clickOrder(order);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void clickOrder(Order order) {
        Cinema cinema=CinemaFactory.getInstance().getById(order.getCinemaId().toString());
        String content = "[" + order.getMovie() + "]" + order.getMovieTime() + "\n" + cinema.toString() + "票价" + order.getPrice() + "元";
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_qrcode,null);
        ImageView img=view.findViewById(R.id.dialog_qrcode_img);
        img.setImageBitmap((AppUtils.createQRCodeBitmap(content, 300, 300)));
        new AlertDialog.Builder(getActivity())
                .setView(view).show();
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
