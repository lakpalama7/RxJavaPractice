package com.svcet.mca.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Observable<Integer> observable=getObservableData();

        Observer<Integer> observer=getObserverData();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer s) throws Exception {
                        return s%3==0?true:false;
                    }
                })
            .subscribeWith(observer);

    }

    private Observer<Integer> getObserverData() {

        return new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                    Log.i("onSubscribe ","subscription");
                    disposable=d;

            }

            @Override
            public void onNext(Integer s) {
                Log.i("Data :", ""+s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("complete ","success");
            }
        };
    }

    private Observable<Integer> getObservableData() {
        return Observable.fromArray(2,4,5,3,7,8,9,1,10,6);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        Log.i("destroy ","dispose ");
    }
}
