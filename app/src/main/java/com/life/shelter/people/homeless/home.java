package com.life.shelter.people.homeless;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.life.shelter.people.homeless.Databeas.Product;
import com.life.shelter.people.homeless.recycleadapter.listadapter;
import com.life.shelter.people.homeless.util.IabHelper;
import com.life.shelter.people.homeless.util.IabResult;
import com.life.shelter.people.homeless.util.Inventory;
import com.life.shelter.people.homeless.util.Purchase;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView serchrecycle;
    RecyclerView.LayoutManager mLayoutManager;
    EditText serchesit;
    listadapter myadapter;
    SearchView searchView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<Product> mylistfinal;

    RecyclerView mylist;
    private static final String TAG = "InAppBilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "com.salim3dd.btnclickme";//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mylistfinal = new ArrayList<>();
        myadapter = new listadapter(this);


        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhW5Q0OlZ9BW3rtylifmWcLabwamc/ztz8PfrxFttxoO44gynEigZbZgczvjz2uNqjtoGMK1I83nxPH7+qZnwyOY5ih9M6o/8MicnKd6yq2/4NwLD1eQxNr9E0J0RT00mj8JWiPGrwO3rDGu61s4o99CdaJRdRVzjnY/QNs0H2idXT12cbGdnIia8OEWQvE+SuHV6QN4Ofdu/drus/REnIHNPiXyZAlXmwezrQxatL6xJ95jJnTZtG1WlDsvbvAKQsHkRFAVLJFTzflgzYkMeujjDO+gIlBQ/iUHlkKg24TBWXRZAOinSlxLN2/zEd3ERJ8ex0pCIvJkgAI3aVcF74QIDAQAB";
///القيمة هنا تتغير مثل حساب المطور

        mHelper = new IabHelper(home.this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");


        reference.child("shelter").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Product info = dataSnapshot.getValue(Product.class);
                mylistfinal.add(info);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // printhasjkey();
        serchrecycle = findViewById(R.id.listhomelessinfo);
        searchView = findViewById(R.id.search); // inititate a search view
        CharSequence query = searchView.getQuery();
        searchView.setQueryHint("Search for homeless by city");
        //  boolean isIconfied=searchView.isIconfiedByDefault();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                final ArrayList<Product> example = mylistfinal;
                ArrayList<Product> example2 = new ArrayList<>();

                text = text.toLowerCase(Locale.getDefault());

                if (text.length() == 0) {
                    myadapter.setmylist(example);
                    serchrecycle.setAdapter(myadapter);
                    myadapter.notifyDataSetChanged();

                } else {
                    for (Product wp : example) {
                        if (wp.getCity().toLowerCase(Locale.getDefault()).contains(text)) {
                            example2.add(wp);

                        }
                        myadapter.setmylist(example2);
                        serchrecycle.setAdapter(myadapter);
                        myadapter.notifyDataSetChanged();
                    }
                }
                //  myadapter.notifyDataSetChanged();


                return false;
            }
        });

        serchrecycle.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        serchrecycle.setLayoutManager(mLayoutManager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                home.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mylist = findViewById(R.id.listhomelessinfo);
        mylist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mylist.setLayoutManager(mLayoutManager);
        mylist.setAdapter(new listadapter(home.this));


    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
            }
        }

        IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
                = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {

                if (result.isFailure()) {
                    // Handle failure
                } else {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                            mConsumeFinishedListener);
                }
            }
        };

        IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
                new IabHelper.OnConsumeFinishedListener() {
                    public void onConsumeFinished(Purchase purchase,
                                                  IabResult result) {

                        if (result.isSuccess()) {


                        } else {
                            // handle error
                        }
                    }
                };


        public void consumeItem() {
            mHelper.queryInventoryAsync(mReceivedInventoryListener);
        }

        public void printhasjkey() {
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.life.shelter.people.homeless",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                mPurchaseFinishedListener.onBackPressed();
            }
        }

    };


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final String appUrl = "https://play.google.com/store/apps/details?id=com.life.shelter.people.homeless";

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_account) {
            Intent it = new Intent(home.this, Account.class);
            startActivity(it);
        } else if (id == R.id.nav_about) {
            Intent it = new Intent(home.this, About.class);
            startActivity(it);
        } else if (id == R.id.nav_faq) {

            Intent it = new Intent(home.this, FAQ.class);
            startActivity(it);
        } else if (id == R.id.nav_charitable) {

            Intent it = new Intent(home.this, CharitableOrganizations.class);
            startActivity(it);
        } else if (id == R.id.nav_supporting) {

            Intent it = new Intent(home.this, Supporting.class);
            startActivity(it);
        } else if (id == R.id.nav_developers) {

            Intent it = new Intent(home.this, Developers.class);
            startActivity(it);
        } else if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl);
            startActivity(Intent.createChooser(shareIntent, "Share using"));

        } else if (id == R.id.nav_rate) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(home.this);
            dialogBuilder.setTitle("Rate Us");
            dialogBuilder.setMessage("If you liked it, please rate it.");
            dialogBuilder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(appUrl));
                    startActivity(i);
                    dialog.dismiss();
                }
            });
           AlertDialog dialog= dialogBuilder.create();
            dialog.show();

                } else if(id ==R.id.nav_out)

                {
                    mAuth.getInstance().signOut();
                    Intent it = new Intent(home.this, Login.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    finish();
                    startActivity(it);
                } else if(id ==R.id.aDonate)

                {
                    Random Rand = new Random();

                    int Rndnum = Rand.nextInt(10000) + 1;
                    mHelper.launchPurchaseFlow(home.this, ITEM_SKU, 10001,
                            mPurchaseFinishedListener, "token-" + Rndnum);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }


        }



