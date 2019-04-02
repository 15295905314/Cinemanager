package net.lzzy.cinemanager.constants.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddOrdersFragment extends BaseFragment {
    private EditText edtMovie;
    private EditText edtPrice;
    private TextView tvTime;
    private Spinner spCinema;
    private ImageView imgQRcode;
    private Button btn;

    private List<Cinema> cinemas;
    public CustomDatePicker datePicker;
    private OnFragmentInteractionListener listener;
    private OnOrderCreatedListener orderListener;

    @Override
    protected void populate() {
        listener.hideSearch();
        initView();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_orders;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            listener.hideSearch();
        }
        cinemas.clear();
        cinemas.addAll(CinemaFactory.getInstance().get());
    }

    private void initView() {
        edtMovie = find(R.id.order_dialog_edt_movie);
        edtPrice = find(R.id.order_dialog_edt_price);
        find(R.id.order_dialog_layoutTime).setOnClickListener(v -> {
            datePicker.show(tvTime.getText().toString());
        });
        tvTime = find(R.id.order_dialog_movieTime);
        spCinema = find(R.id.order_dialog_btn_spinnerArea);
        imgQRcode = find(R.id.order_dialog_Iv);
        btn = find(R.id.order_dialog_btn_ok);
        initDatePicker();
        Dialog();

    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvTime.setText(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        String end = sdf.format(calendar.getTime());
        datePicker = new CustomDatePicker(getActivity(), s -> tvTime.setText(s), now, end);
        datePicker.showSpecificTime(true);
        datePicker.setIsLoop(true);
    }

    private void Dialog() {
        cinemas = CinemaFactory.getInstance().get();
        spCinema.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, cinemas));

        find(R.id.order_dialog_btn_3).setOnClickListener(v -> {
            orderListener.cancelAddOrder();
        });
        find(R.id.order_dialog_btn_ok).setOnClickListener(v -> {
            Order order = new Order();
            String movie = edtMovie.getText().toString();
            String time = tvTime.getText().toString();
            if (TextUtils.isEmpty(movie) || TextUtils.isEmpty(time)) {
                Toast.makeText(getActivity(), "信息不全", Toast.LENGTH_SHORT).show();
                return;
            }
            Float price;
            try {
                price = Float.parseFloat(edtPrice.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "数字格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cinemas.size() != 0) {
                Cinema cinema = cinemas.get(spCinema.getSelectedItemPosition());
                order.setMovie(movie);
                order.setMovieTime(time);
                order.setPrice(price);
                order.setCinemaId(cinema.getId());
                orderListener.saveOrder(order);
                edtMovie.setText("");
                edtPrice.setText("");
            } else {
                Toast.makeText(getActivity(), "请添加影院", Toast.LENGTH_SHORT).show();

            }
        });
        find(R.id.order_dialog_btn_er).setOnClickListener(v -> {
            String name = edtMovie.getText().toString();
            String price = edtPrice.getText().toString();
            String location = spCinema.getSelectedItem().toString();
            String time = tvTime.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(location) || TextUtils.isEmpty(time)) {
                Toast.makeText(getActivity(), "信息不全", Toast.LENGTH_SHORT).show();
                return;
            }
            String content = "[" + name + "]" + time + "\n" + location + "票价" + price + "元";
            imgQRcode.setImageBitmap(AppUtils.createQRCodeBitmap(content, 200, 200));
        });
        imgQRcode.setOnLongClickListener(v -> {
            Bitmap bitmap = ((BitmapDrawable) imgQRcode.getDrawable()).getBitmap();
            Toast.makeText(getActivity(), AppUtils.readQRCode(bitmap), Toast.LENGTH_SHORT).show();
            return true;
        });


    }


    @Override
    public void search(String kw) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            orderListener = (OnOrderCreatedListener) context;
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "必需实现OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        orderListener = null;
    }

    public interface OnFragmentInteractionListener{
        void hideSearch();
    }

    public interface OnOrderCreatedListener {
        /**
         * 点击取消保存
         */
        void cancelAddOrder();

        /**
         * 点击保存
         */
        void saveOrder(Order order);
    }


}
