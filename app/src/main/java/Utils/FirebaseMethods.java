package Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hostelapp.UserRegistration;
import com.example.hostelapp.Verification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import models.User;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;

    private String userID, admission_number;

    private Context mContext;
    public FirebaseMethods(Context context)
    {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Register new user with admission email and password to Firebase Authetication
     * @param admissionnumber
     * @param password
     * @param email
     */
    public void registerNewUser(final String admissionnumber, String password, final String email)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();

                            Log.d(TAG, "onComplete: Authstate changed : "+ userID);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public String checkAdmissionNumber(String admission_number)
    {
       documentReference = firebaseFirestore.collection("registered").document(admission_number);
       documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               Log.d(TAG, "onComplete: Admssion number + "+documentSnapshot.get("adnumber"));
               Toast.makeText(mContext, ""+documentSnapshot.get("adnumber"),Toast.LENGTH_SHORT);
           }

       }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(mContext, "Didnt work",Toast.LENGTH_SHORT);
                   }
               });
       return admission_number;
    }


public void addNewUser(String email, String phone, String admission_number, String room, String block, String hostel, String department, String name, String semester){
    User user = new User(userID, phone, email, admission_number, room, block, hostel, semester, department, name);
    documentReference = firebaseFirestore.collection("users").document(userID);
    documentReference.set(user);

}

}
