package net.lzzy.cinemanager.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.constants.fragments.CinemaOrdersFragment;

/**
 * Created by lzzy_gxy on 2019/4/3.
 * Description:
 */
public class CinemaOrdersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cinema_orders);
        //activity_cinema_orders
        String cinemaId=getIntent().getStringExtra(MainActivity.EXTRA_CINEMA_ID);
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentById(R.id.cinema_orders_conteiner);
        //cinema_orders_container
        if (fragment==null){
            fragment= CinemaOrdersFragment.newInstance(cinemaId);
            manager.beginTransaction().add(R.id.cinema_orders_conteiner,fragment).commit();
            //cinema_orders_container
        }
    }

}
