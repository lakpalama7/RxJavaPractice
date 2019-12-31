package com.svcet.mca.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.svcet.mca.rxjavapractice.model.NoteModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        getObservableData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<User, User>() {
                    @Override
                    public User apply(User user) throws Exception {
                        user.setName(user.getName().toUpperCase());
                        user.setEmail(String.format("%s@esecforte.com",user.getName()));

                        return user;
                    }
                }).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(User user) {
                Log.i("User details :",user.getId() +" : "+user.getName() +" : "+user.getEmail());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("Success ","Complete");
            }
        });
    }
//getting data from network lets suppose....
    private Observable<User> getObservableData(){

        String [] names={"ram","hari","ganesh","radhe"};
        final List<User> userlist=new ArrayList<>();
        int count=1;

        for(String name:names){
            User note=new User();
            note.setId(count);
            note.setName(name);
            count++;
            userlist.add(note);
        }
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                if(!emitter.isDisposed()){
                    for(User user:userlist){
                        emitter.onNext(user);
                        Thread.sleep(2000);
                    }
                }
                if(!emitter.isDisposed()){
                    emitter.onComplete();
                }
            }
        });
    }



    private class User{
        private int id;
        private String name;
        private String email;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
