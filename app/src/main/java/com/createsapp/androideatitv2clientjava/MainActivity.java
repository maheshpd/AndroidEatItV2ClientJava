package com.createsapp.androideatitv2clientjava;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.createsapp.androideatitv2clientjava.common.Common;
import com.createsapp.androideatitv2clientjava.model.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private static int APP_REQUEST_CODE = 7171; //anu number
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private DatabaseReference userRef;
    private List<AuthUI.IdpConfig> providers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        compositeDisposable.clear();


    }

    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCE);
        firebaseAuth = FirebaseAuth.getInstance();

        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
        listener = firebaseAuth -> {

            Dexter.withContext(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                checkUserFromFirebase(user);
                            } else {
                                phoneLogin();
                            }
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(MainActivity.this, "You must enable this permission to use app", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    }).check();


        };
    }

    private void checkUserFromFirebase(FirebaseUser user) {
        dialog.show();
        userRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(MainActivity.this, "You already register", Toast.LENGTH_SHORT).show();
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            goToHomeActivity(userModel);
                        } else {
                            showRegisterDialog(user);
                        }

                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRegisterDialog(FirebaseUser user) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Register");
        builder.setMessage("Please fill information");
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        final EditText edt_name = itemView.findViewById(R.id.edt_name);
        final EditText edt_address = itemView.findViewById(R.id.edt_address);
        final EditText edt_phone = itemView.findViewById(R.id.edt_phone);

        //Set
        edt_phone.setText(user.getPhoneNumber());
        builder.setView(itemView);
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("REGISTER", (dialog, which) -> {
            if (TextUtils.isEmpty(edt_name.getText().toString())) {
                Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(edt_address.getText().toString())) {
                Toast.makeText(MainActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                return;
            }

            final UserModel userModel = new UserModel();
            userModel.setUid(user.getUid());
            userModel.setName(edt_name.getText().toString());
            userModel.setAddress(edt_address.getText().toString());
            userModel.setPhone(edt_phone.getText().toString());

            userRef.child(user.getUid()).setValue(userModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();

                                Toast.makeText(MainActivity.this, "Congratulation ! Register success", Toast.LENGTH_SHORT).show();
                                goToHomeActivity(userModel);
                            }
                        }
                    });

        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToHomeActivity(UserModel userModel) {
        Common.currentUser = userModel; //Important, you need always assign value for it before use
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "Failed to sign in!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void phoneLogin() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build(), APP_REQUEST_CODE);
    }
}