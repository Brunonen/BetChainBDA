package bda.hslu.ch.betchain;

import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import bda.hslu.ch.betchain.DTO.Participant;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String betCreationBetTitle = "";
    private String betCreationBetConditions = "";
    private float betCreationBetEntryFee = 0.0f;
    private List<Participant> betCreationParticipants = new ArrayList<Participant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new LoginFragment();

        changeFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        int id = item.getItemId();


        if (id == R.id.menu_myBets) {
            fragment = new MyBetsFragment();
        } else if (id == R.id.menu_createBet) {
            fragment = new CreateBetStep1Fragment();
        } else if (id == R.id.menu_friends) {
            fragment = new FriendsFragment();
        } else if (id == R.id.menu_account) {
            fragment = new AccountInfoFragment();
        } else if (id == R.id.menu_settings) {
            fragment = new LoginFragment();
        } else if (id == R.id.menu_logout) {
            fragment = new LoginFragment();
        }

        if(fragment != null) {
            changeFragment(fragment);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("LastFragment").commit();

    }

    public void setBetCreationBetTitle(String betTitle){
        this.betCreationBetTitle = betTitle;
    }

    public void setBetCreationBetConditions(String betConditions){
        this.betCreationBetConditions = betConditions;
    }

    public void setBetCreationBetEntryFee(float entryFee){
        this.betCreationBetEntryFee = entryFee;
    }

    public void setBetCreationParticipants(List<Participant> participants){
        this.betCreationParticipants =participants;
    }

    public String getBetCreationBetTitle(){
        return this.betCreationBetTitle;
    }

    public String getBetCreationBetConditions(){
        return this.betCreationBetConditions;
    }

    public float getBetCreationBetEntryFee(){
        return this.betCreationBetEntryFee;
    }

    public List<Participant> getBetCreationParticipants(){
        return this.betCreationParticipants;
    }

    public void addParticipantToList(Participant part){
        this.betCreationParticipants.add(part);
    }

    public void removeParticipantFromList(Participant part){
        this.betCreationParticipants.remove(part);
    }
}
