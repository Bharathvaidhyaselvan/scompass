package com.packages.scompass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MomentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    public static String userName;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);

        SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);
        userName = s.getString("user_name","");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        FloatingActionButton uploadFab = findViewById(R.id.upload_fab);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.moments_menu) {
                return true;
            } else if (itemId == R.id.home_menu) {
                startActivityWithSelectedMenuItem(HomeActivity.class, R.id.home_menu);
                return true;
            } else if (itemId == R.id.package_menu) {
                startActivityWithSelectedMenuItem(MomentsActivity.class, R.id.package_menu);
                return true;
            } else if (itemId == R.id.map_menu) {
                startActivityWithSelectedMenuItem(MapsActivity.class, R.id.map_menu);
                return true;
            } else if (itemId == R.id.about_menu) {
                startActivityWithSelectedMenuItem(AboutActivity.class, R.id.about_menu);
                return true;
            }
            return false;
        });
        int selectedMenuItemId = getIntent().getIntExtra("selectedMenuItemId", R.id.moments_menu);
        bottomNavigationView.setSelectedItemId(selectedMenuItemId);


        uploadFab.setOnClickListener(view -> {
            startActivity(new Intent(this, UploadActivity.class).putExtra("user_name",userName));
            finish();
        });


        swipeRefreshLayout.setOnRefreshListener(this::LoadPosts);

        new Handler().postDelayed(this::LoadPosts, 1000);
    }

    private void startActivityWithSelectedMenuItem(Class<?> destinationActivity, int selectedMenuItemId) {
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra("selectedMenuItemId", selectedMenuItemId);
        startActivity(intent);
    }

    public void LoadPosts()
    {

        ArrayList<String> POSTID = new ArrayList<>();
        ArrayList<String> POSTIMAGE = new ArrayList<>();
        ArrayList<String> PROFILEPHOTO = new ArrayList<>();
        ArrayList<String> CAPTION = new ArrayList<>();
        ArrayList<String> USERNAME = new ArrayList<>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot postid: snapshot.getChildren())
                    {
                        DatabaseReference imgurldb = db.child(postid.getKey()).child("imageUrl");
                        DatabaseReference profilephotodb = db.child(postid.getKey()).child("profileImageUrl");
                        DatabaseReference captiondb = db.child(postid.getKey()).child("caption");
                        DatabaseReference usernamedb = db.child(postid.getKey()).child("username");

                        imgurldb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotpostimg) {
                                if (snapshotpostimg.exists())
                                {
                                    profilephotodb.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshotprofileimg) {
                                            if (snapshotprofileimg.exists())
                                            {
                                                captiondb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshotcaption) {
                                                        if (snapshotcaption.exists())
                                                        {
                                                            usernamedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshotusername) {
                                                                    if (snapshotusername.exists())
                                                                    {

                                                                        POSTID.add(postid.getKey());
                                                                        POSTIMAGE.add(snapshotpostimg.getValue(String.class));
                                                                        PROFILEPHOTO.add(snapshotprofileimg.getValue(String.class));
                                                                        CAPTION.add(snapshotcaption.getValue(String.class));
                                                                        USERNAME.add(snapshotusername.getValue(String.class));

                                                                        SetPost setPost = new SetPost(MomentsActivity.this,
                                                                                POSTID,
                                                                                POSTIMAGE,
                                                                                PROFILEPHOTO,
                                                                                CAPTION,
                                                                                USERNAME);
                                                                        recyclerView.setAdapter(setPost);

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    Toast.makeText(MomentsActivity.this, "Oops, Curreently No Post to Show!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

class SetPost extends RecyclerView.Adapter<SetPost.SetPostChild>
{
    Context context;
    ArrayList<String> POSTID = new ArrayList<>();
    ArrayList<String> POSTIMAGE = new ArrayList<>();
    ArrayList<String> PROFILEPHOTO = new ArrayList<>();
    ArrayList<String> CAPTION = new ArrayList<>();
    ArrayList<String> USERNAME = new ArrayList<>();

    int commentsVisibility=0;

    public SetPost(Context context, ArrayList<String> POSTID, ArrayList<String> POSTIMAGE, ArrayList<String> PROFILEPHOTO, ArrayList<String> CAPTION, ArrayList<String> USERNAME) {
        this.context = context;
        this.POSTID = POSTID;
        this.POSTIMAGE = POSTIMAGE;
        this.PROFILEPHOTO = PROFILEPHOTO;
        this.CAPTION = CAPTION;
        this.USERNAME = USERNAME;
     }

    @NonNull
    @Override
    public SetPostChild onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_post, parent, false);
        return new SetPostChild(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetPostChild holder, @SuppressLint("RecyclerView") int position) {
        holder.LIKES = new ArrayList<>();
        DatabaseReference likesdb = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position)).child("likes");
        likesdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    int total=0;
                    for (DataSnapshot likeid: snapshot.getChildren())
                    {
                        total++;
                        holder.likesCountTextView.setText(String.valueOf(total));
                        holder.LIKES.add(likeid.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Populate the UI elements with post data
        holder.captionTextView.setText(CAPTION.get(position));
        // Set the username to the usernameTextView
        holder.usernameTextView.setText(USERNAME.get(position)); // Use the userName variable
        // Load the post image using Glide (you may need to add Glide to your dependencies)
        Glide.with(context)
                .load(POSTIMAGE.get(position))
                .into(holder.postImageView);
        // Show the image if is not -1 in imageview.
        // If the post uploader has no image, we get -1 in string.
        if (!PROFILEPHOTO.get(position).equals("-1"))
        {
            Glide.with(context)
                    .load(PROFILEPHOTO.get(position))
                    .into(holder.postProfileImageView);
        }
        GetLikeandComments(position,holder,context);

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    // User is signed in
                    String userEmail = currentUser.getEmail();

                    if (userEmail != null) {
                        String[] parts = userEmail.split("@");

                        if (parts.length > 0) {
                            String idBeforeAtSymbol = parts[0];
                            if (holder.LIKES.size()!=0)
                            {
                                if (holder.LIKES.contains(idBeforeAtSymbol))
                                {
                                    Toast.makeText(context, "Post disliked", Toast.LENGTH_SHORT).show();
                                    holder.likesCountTextView.setText(String.valueOf(Integer.parseInt(holder.likesCountTextView.getText().toString().trim())-1));
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
                                    DatabaseReference likes = db.child("likes").child(idBeforeAtSymbol);
                                    holder.LIKES.remove(idBeforeAtSymbol);
                                    likes.setValue(null);
                                }
                                else {
                                    Toast.makeText(context, "Post liked", Toast.LENGTH_SHORT).show();
                                    holder.likesCountTextView.setText(String.valueOf(Integer.parseInt(holder.likesCountTextView.getText().toString().trim())+1));
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
                                    DatabaseReference likes = db.child("likes").child(idBeforeAtSymbol);
                                    holder.LIKES.add(idBeforeAtSymbol);
                                    likes.setValue("1");
                                }
                            }
                            else {
                                Toast.makeText(context, "Post liked", Toast.LENGTH_SHORT).show();
                                holder.likesCountTextView.setText(String.valueOf(Integer.parseInt(holder.likesCountTextView.getText().toString().trim())+1));
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
                                DatabaseReference likes = db.child("likes").child(idBeforeAtSymbol);
                                holder.LIKES.add(idBeforeAtSymbol);
                                likes.setValue("1");
                            }

                        }
                    } else {
                        Toast.makeText(context, "Something went's wrong!", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(context, "Something went's wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentsVisibility==0)
                {
                    holder.recyclerviewComments.setVisibility(View.VISIBLE);
                     commentsVisibility=1;
                     if (holder.commentsCountTextView.getText().toString().trim().equals("0"))
                     {
                         Toast.makeText(context, "No comments yet!", Toast.LENGTH_SHORT).show();
                     }
                     
                }
                else {
                    commentsVisibility=0;
                    holder.recyclerviewComments.setVisibility(View.GONE);
                }
            }
        });

        holder.sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.addcomment.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(context, "Field required is empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position)).child("comments").push();
                    DatabaseReference value = db.child("value"); value.setValue(holder.addcomment.getText().toString().trim());
                    DatabaseReference usernamecomment = db.child("username"); usernamecomment.setValue(MomentsActivity.userName);
                    Toast.makeText(context, "Comment Posted", Toast.LENGTH_SHORT).show();
                    holder.addcomment.setText("");
                    GetLikeandComments(position,holder,context);
                }

            }
        });
    }

    public void GetLikeandComments(int position, SetPostChild holder,Context c)
    {
        ArrayList<String> COMMENTID = new ArrayList<>();
        ArrayList<String> VALUE = new ArrayList<>();
        ArrayList<String> COMMENTUSERNAME = new ArrayList<>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
        DatabaseReference commments = db.child("comments");

        commments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    // likes exists
                    int com=0;
                    for (DataSnapshot commentid: snapshot.getChildren())
                    {
                        com++;
                        DatabaseReference value = commments.child(commentid.getKey()).child("value");
                        DatabaseReference username = commments.child(commentid.getKey()).child("username");
                        int finalCom = com;
                        value.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotvalue) {
                                if (snapshotvalue.exists())
                                {
                                    username.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshotusername) {
                                            if (snapshotusername.exists())
                                            {
                                                holder.commentsCountTextView.setText(String.valueOf(finalCom));
                                                COMMENTID.add(commentid.getKey());
                                                VALUE.add(snapshotvalue.getValue(String.class));
                                                COMMENTUSERNAME.add(snapshotusername.getValue(String.class));
                                                SetComments setComments = new SetComments(c,COMMENTID,VALUE,COMMENTUSERNAME);
                                                holder.recyclerviewComments.setAdapter(setComments);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                     }
                }
                else {
                    // no likes
                    holder.commentsCountTextView.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return POSTID.size();
    }

    static class SetPostChild extends PostAdapter.ViewHolder{
        ArrayList<String> LIKES;
        EditText addcomment;
        TextView sendcomment;
        ImageView postImageView;
        // declare variable for profile name and image
        ImageView postProfileImageView;
        TextView usernameTextView;
        TextView captionTextView;
        TextView likesCountTextView;
        TextView commentsCountTextView;
        ImageView likeButton,commentButton;
        RecyclerView recyclerviewComments;
        public SetPostChild(@NonNull View v) {
            super(v);
            recyclerviewComments = itemView.findViewById(R.id.recyclerviewComments);
            sendcomment = itemView.findViewById(R.id.sendcomment);
            postImageView = itemView.findViewById(R.id.post_image);
            addcomment = itemView.findViewById(R.id.addcomment);
            postProfileImageView = itemView.findViewById(R.id.profile_post_image);
            captionTextView = itemView.findViewById(R.id.post_caption);
            usernameTextView = itemView.findViewById(R.id.post_username);
            likesCountTextView = itemView.findViewById(R.id.post_likes);
            commentsCountTextView = itemView.findViewById(R.id.post_comments);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
        }
    }
}
class  SetComments extends RecyclerView.Adapter<SetComments.SetCommentsChild>
{

    Context c;
    ArrayList<String> COMMENTID = new ArrayList<>();
    ArrayList<String> VALUE = new ArrayList<>();
    ArrayList<String> COMMENTUSERNAME = new ArrayList<>();

    public SetComments(Context c, ArrayList<String> COMMENTID, ArrayList<String> VALUE, ArrayList<String> COMMENTUSERNAME) {
        this.c = c;
        this.COMMENTID = COMMENTID;
        this.VALUE = VALUE;
        this.COMMENTUSERNAME = COMMENTUSERNAME;
    }

    @NonNull
    @Override
    public SetCommentsChild onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new SetCommentsChild(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SetCommentsChild holder, int position) {
        holder.name.setText(COMMENTUSERNAME.get(position));
        holder.value.setText(VALUE.get(position));
    }

    @Override
    public int getItemCount() {
        return COMMENTID.size();
    }

    static class SetCommentsChild extends PostAdapter.ViewHolder{
        TextView name,value;
        public SetCommentsChild(@NonNull View v) {
            super(v);
            name = itemView.findViewById(R.id.username);
            value = itemView.findViewById(R.id.commentTextTextView);
        }
    }
}