package com.increff.pos.api;

import javax.transaction.Transactional;

@Transactional(rollbackOn = ApiException.class)
public abstract class AbstractApi<T>{



}
