package com.example.davideshett.david;



import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {



    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;
    private CircleImageView circleImageView;
    private TextView nameTv,emailTv,idTv;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, null);

        circleImageView = view.findViewById(R.id.profile_image);
        nameTv = view.findViewById(R.id.username_txt);
        emailTv = view.findViewById(R.id.user_email);
        idTv = view.findViewById(R.id.user_id);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()!= null){

                     updateUi();
                }

            }
        };



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }


    public void updateUi() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();


            nameTv.setText(personGivenName + " " + personFamilyName);
            emailTv.setText(personEmail);
            idTv.setText(personId);

            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();



            if (mUser != null) {
                for (UserInfo profile : mUser.getProviderData()) {


                    Uri photoUrl = profile.getPhotoUrl();


                    String originalPieceOfUrl = "s96-c/photo.jpg";

                     String newPieceOfUrlToAdd = "s400-c/photo.jpg";

                    // Check if the Url path is null
                    if (photoUrl != null) {

                        String photoPath = photoUrl.toString();

                        String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                        Picasso.with(getContext()).load(newString).placeholder(R.drawable.place).fit()
                                .into(circleImageView);

                    }
                }

            }

        }
    }
}
