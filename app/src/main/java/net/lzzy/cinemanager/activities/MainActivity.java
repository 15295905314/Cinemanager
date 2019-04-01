package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.constants.fragments.AddCinemasFrament;
import net.lzzy.cinemanager.constants.fragments.AddOrdersFragment;
import net.lzzy.cinemanager.constants.fragments.BaseFragment;
import net.lzzy.cinemanager.constants.fragments.CinemasFrament;
import net.lzzy.cinemanager.constants.fragments.OrdersFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.utils.ViewUtils;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , AddCinemasFrament.OnFragmentInteractionListener, AddCinemasFrament.OnCinemaCreatedListener {
    private View layoutMenu;
    private SearchView search;
    private TextView tvTitle;
    private FragmentManager manager = getSupportFragmentManager();
    private SparseArray<String> titleArray = new SparseArray<>();
    private SparseArray<Fragment> fragmentArray = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment =manager.findFragmentById(R.id.fragment_container);
                if (fragment!=null) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).search(kw);
                    }
                }
                return true;
            }
        });


    }

    private void setTitleMenu() {
        layoutMenu = findViewById(R.id.bar_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_img_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visible = layoutMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                layoutMenu.setVisibility(visible);
            }
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
        tvTitle.setText("我的订单");
        search = findViewById(R.id.bar_searchView);
        findViewById(R.id.bar_order).setOnClickListener(this);
        findViewById(R.id.bar_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_add_order).setOnClickListener(this);
        findViewById(R.id.bar_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArray.get(v.getId()));

        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = fragmentArray.get(v.getId());
        if (fragment == null) {
            fragment = createFragment(v.getId());
            fragmentArray.put(v.getId(), fragment);
            transaction.add(R.id.fragment_container, fragment);
        }
        for (Fragment f : manager.getFragments()) {
            transaction.hide(f);
        }
        transaction.show(fragment).commit();
    }


    private Fragment createFragment(int id) {
        switch (id) {
            case R.id.bar_add_cinema:
                return new AddCinemasFrament();

            case R.id.bar_view_cinema:
                return new CinemasFrament();

            case R.id.bar_add_order:
                return new AddOrdersFragment();
            case R.id.bar_order:
                return new OrdersFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public void hideSearch() {
        search.setVisibility(View.INVISIBLE);
    }

    @Override
    public void cencelAddCinema() {
        Fragment addCinemasFragment = fragmentArray.get(R.id.bar_add_cinema);
        if (addCinemasFragment == null) {
            return;
        }
        Fragment cinemasFragment = fragmentArray.get(R.id.bar_view_cinema);
        FragmentTransaction transaction = manager.beginTransaction();
        if (cinemasFragment == null) {
            cinemasFragment = new CinemasFrament();
            fragmentArray.put(R.id.bar_view_cinema, cinemasFragment);
            transaction.add(R.id.fragment_container, cinemasFragment);
        }
        transaction .hide(addCinemasFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_view_cinema));

    }

    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemaFragment=fragmentArray.get(R.id.bar_add_cinema);
        if (addCinemaFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_view_cinema);
        FragmentTransaction transaction =manager.beginTransaction();
        if (cinemasFragment==null){
            cinemasFragment=new CinemasFrament(cinema);
        }else {
            ((CinemasFrament)cinemasFragment).svae(cinema);
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_view_cinema));
    }
}
