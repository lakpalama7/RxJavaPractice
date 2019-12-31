package com.svcet.mca.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FlatMapActivity extends AppCompatActivity {

    private Disposable disposable;
  //  int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map);


        getUserData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<User, Observable<User>>() {
                    @Override
                    public Observable<User> apply(User user) throws Exception {
                        return newObserableUser(user);
                    }
                }).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("Subscribe ","onsubscribe called ");
                disposable=d;
            }

            @Override
            public void onNext(User user) {
                Log.i("user details : ",user.getName() +" : "+user.getGender() +" : "+user.getAddress().getAddress());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("Complete : ","success ");
            }
        });


    }

    private Observable<User> newObserableUser(final User user) {
        final String [] addresses={"banglore","chittoor","chenai","mumbai","delhi"};
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                if(!emitter.isDisposed()){
                    Address address=new Address();
                  //  Log.i("Number : ",""+new Random().nextInt(5));
                    address.setAddress(addresses[new Random().nextInt(5)]);
                    user.setAddress(address);
                    emitter.onNext(user);
                   // count++;
                    int sleeptime=new Random().nextInt(3000);
                    Thread.sleep(sleeptime);

                }
                emitter.onComplete();


            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<User> getUserData(){
        String [] names={"ram","hari","anil","sunil","raj"};

        final List<User> userlist=new ArrayList<>();
        for(String username:names){
            User user=new User();
            user.setName(username);
            user.setGender("male");
            userlist.add(user);
        }
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                    if(!emitter.isDisposed()){
                        for(User user:userlist){
                            emitter.onNext(user);
                        }
                    }emitter.onComplete();
                   /* if (!emitter.isDisposed()){
                        emitter.onComplete();
                    }*/
            }
        }).subscribeOn(Schedulers.io());
    }

    private class User{
        private String name;
        private String gender;
        private Address address;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    private class Address{
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
