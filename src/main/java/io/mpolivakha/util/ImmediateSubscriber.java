package io.mpolivakha.util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class ImmediateSubscriber<T> implements Subscriber<T> {

  @Override
  public void onSubscribe(Subscription subscription) {
    subscription.request(Integer.MAX_VALUE);
  }


  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {

  }
}
