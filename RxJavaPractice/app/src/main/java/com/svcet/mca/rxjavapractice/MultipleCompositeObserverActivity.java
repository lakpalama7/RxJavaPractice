package com.svcet.mca.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MultipleCompositeObserverActivity extends AppCompatActivity {


    private CompositeDisposable compositeDisposable=new CompositeDisposable(); //holds multiple observer subscrition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposable_observer);


        Observable<String> observable=getObservableData();

        DisposableObserver<String> startwithB=getDataStartwithB();


        DisposableObserver<String> uppdercaseC=getDataStartwithC();

        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("b");
                            }
                        }).subscribeWith(startwithB));



        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("c");
                            }
                        })
                        .map(new Function<String, String>() {

                            @Override
                            public String apply(String s) throws Exception {
                                return s.toUpperCase();
                            }
                        }).subscribeWith(uppdercaseC));

    }
    private DisposableObserver<String> getDataStartwithB() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i("Start with b ", s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("Complete start B","success");
            }
        };
    }

    private DisposableObserver<String> getDataStartwithC() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i("Upper Case C :",s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("Complete start C ","success ");

            }
        };
    }




    private Observable<String> getObservableData() {
        return Observable.fromArray("cat","ball","bat","bcc","bbc","camel","camera","tiger","carrot","");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        Log.i("Destroy :","compositedisposable destroy");
    }
}
