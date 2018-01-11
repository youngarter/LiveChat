package com.bringletech.looviedraft.livechat.ui.ENDLESS;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;


import com.bringletech.looviedraft.livechat.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class DashBoard extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private RecyclerView recyclerView;
    private UsersAdapter adapter;

    RecyclerView.LayoutManager mLayoutManager;

    //### Detect Endless items

   // boolean loading = true;
    //int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //### Initialisation du package FRESCO
        Fresco.initialize(this);
        setContentView(R.layout.users_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        FAMOUS.UserList = new ArrayList<>();
        adapter = new UsersAdapter(this,FAMOUS.UserList);

        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //CALL FirebaseSaveBack to get users
        FirebaseSaveBack.prepareUsers(false,adapter,this);
        detectScrollEnd();

        try {
       //     Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Detect The Onscroll Event and load the lines needed
     */
    private void detectScrollEnd() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                   //=========================================================
                    int visibleItemCount = recyclerView.getChildCount();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    if (FAMOUS.mLoading) {
                        if (totalItemCount > FAMOUS.mPreviousTotal) {
                            FAMOUS.mLoading = false;
                            FAMOUS.mPreviousTotal = totalItemCount;
                        }
                    }
                    int visibleThreshold = 5;
                    if (!FAMOUS.mLoading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                       FirebaseSaveBack.prepareUsers(true,adapter,DashBoard.this);
                        FAMOUS.mLoading = true;
                    }
                    //=========================================================
                }
            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few Users for testing

    private void prepareUsers( boolean nextItems) {

        //### CALL instance of Firebase database (TEST)
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query queryRef;
        if(!nextItems){
            queryRef = mDatabase.child("userstest").orderByKey().limitToFirst(maxUsers) ;
        }else{
            queryRef = mDatabase.child("userstest").orderByKey().startAt(head_first_pack).limitToFirst(maxUsers) ;
        }


        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()){
                        //Log.d(TAG,"FIRST " + dataSnapshot.get);
                        //Log.e(TAG ,"count##"+dataSnapshot.getChildrenCount());
                        int i=0;
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                           // Log.d(TAG,"GET " + postSnapshot.getKey());
                            User usr = postSnapshot.getValue(User.class);
                            UserList.add(usr);
                           // Log.e(TAG, "#### DATA #### Nom  :   "+usr.getNom());
                            if(i==maxUsers-1){
                                head_first_pack = postSnapshot.getKey();
                                Log.d(TAG,"GET  IIII i " + postSnapshot.getKey());
                                i=0;
                            }else{
                                i+=1;
                            }

                        }
                        adapter.notifyDataSetChanged();
                }
         //       Log.w(TAG, dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
     */
    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
