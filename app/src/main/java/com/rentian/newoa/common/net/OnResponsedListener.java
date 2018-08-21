package com.rentian.newoa.common.net;

public interface OnResponsedListener<T>
{

	void onEntityLoadComplete(T entity);

	void onError(T entity);
}
